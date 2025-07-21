/**
 * パッケージ名：org.mbasa.CensusService.repository
 * ファイル名  ：CustomRepository.java
 * 
 * @author mbasa
 * @since Jun 19, 2025
 */
package org.mbasa.CensusService.repository;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 説明：
 *
 */
@Repository
public class CustomRepository {

    /**
     * コンストラクタ
     *
     */
    public CustomRepository() {
    }

    @Autowired
    private EntityManager em;

    private String cleanJson(String geoJson) {
        if (geoJson.startsWith("'") && geoJson.endsWith("'") &&
                geoJson.length() >= 2) {
            geoJson = geoJson.substring(1, geoJson.length() - 1);
        }
        if (geoJson.startsWith("\"") && geoJson.endsWith("\"") &&
                geoJson.length() >= 2) {
            geoJson = geoJson.substring(1, geoJson.length() - 1);
        }

        return geoJson;
    }

    public String queryMesh4(String geoJson) {

        geoJson = this.cleanJson(geoJson);

        String sql = String.format("""
                 WITH json_load AS (
                    SELECT ST_GeomFromGeoJSON('%s'\\:\\:JSONB->'geometry') json_geom
                )
                SELECT CAST(jsonb_build_object(
                    'type',     'FeatureCollection',
                    'features', jsonb_agg(feature)
                ) AS TEXT)
                FROM (
                  SELECT jsonb_build_object(
                    'type',       'Feature',
                    'id',       "MESH_CODE",
                    'geometry',   ST_AsGeoJSON(geom)\\:\\:JSONB,
                    'properties', to_jsonb(row) - "MESH_CODE" - 'geom'
                  ) AS feature
                  FROM (SELECT "MESH_CODE",
                    "人口総数",
                    "０〜１４歳人口総数",
                    "１５歳以上人口総数",
                    "１５〜６４歳人口総数",
                    "２０歳以上人口総数",
                    "６５歳以上人口総数",
                    "７５歳以上人口総数",
                    "外国人人口総数",
                    "世帯総数",
                    "一般世帯数",
                    "１人世帯の一般世帯数",
                    "２人世帯の一般世帯数",
                    "３人世帯の一般世帯数",
                    "４人世帯の一般世帯数",
                    "５人世帯の一般世帯数",
                    "６人世帯の一般世帯数",
                    "７人以上世帯の一般世帯数",
                    "６歳未満世帯員のいる一般世帯数",
                    "６５歳以上世帯員のいる一般世帯数",
                    "世帯主の年齢が２０〜２９歳の１人世帯の一般世帯数",
                    "高齢単身の一般世帯数",
                    "高齢夫婦のみの一般世帯数",
                    geom
                    FROM mesh4 m,json_load j WHERE ST_OVERLAPS(m.geom,j.json_geom)) row) features;
                  """, geoJson.replace("\\\"", "\"").replaceAll("\\s+", ""));

        String retVal = em.createNativeQuery(sql)
                .getSingleResult()
                .toString();

        return retVal;
    }

    public String queryPoi(String geoJson) {

        geoJson = this.cleanJson(geoJson);

        String sql = String.format("""
                 WITH json_load AS (
                    SELECT ST_GeomFromGeoJSON('%s'\\:\\:JSONB->'geometry') json_geom
                )
                SELECT CAST(jsonb_build_object(
                    'type',     'FeatureCollection',
                    'features', jsonb_agg(feature)
                ) AS TEXT)
                FROM (
                  SELECT jsonb_build_object(
                    'type',       'Feature',
                    'id',         osm_id,
                    'geometry',   ST_AsGeoJSON(geom)\\:\\:JSONB,
                    'properties', to_jsonb(row) - 'osm_id' - 'geom'
                  ) AS feature
                  FROM (SELECT osm_id,
                    category,
                    name,
                    geom
                    FROM osm_poi m,json_load j WHERE ST_CONTAINS(j.json_geom,m.geom)) row) features;
                  """, geoJson.replace("\\\"", "\"").replaceAll("\\s+", ""));

        String retVal = em.createNativeQuery(sql)
                .getSingleResult()
                .toString();

        return retVal;
    }

    public String queryPeopleFlow(String geoJson, String fieldName) {
        geoJson = this.cleanJson(geoJson);

        String sql = String.format("""
                 WITH json_load AS (
                    SELECT ST_GeomFromGeoJSON('%s'\\:\\:JSONB->'geometry') geom
                )
                SELECT json_object_agg(case when field is null then 'その他' else field end,count)\\:\\:text as t1
                FROM
                 (SELECT %s as field,count(*) as count
                  FROM
                    (SELECT distinct on (a.userid) c.* FROM geolocation a, json_load b,survey c
                    WHERE st_contains(b.geom,a.geom) and a.userid = c.userid) as q1
                    GROUP BY %s ) as q2  ;
                  """,
                geoJson.replace("\\\"", "\"").replaceAll("\\s+", ""),
                fieldName,
                fieldName);

        String retVal = em.createNativeQuery(sql)
                .getSingleResult()
                .toString();

        return retVal;
    }
}
