/**
 * パッケージ名：org.mbasa.CensusService.controller
 * ファイル名  ：ApplicationController.java
 * 
 * @author mbasa
 * @since Jun 19, 2025
 */
package org.mbasa.CensusService.controller;

import org.mbasa.CensusService.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * 説明：
 *
 */
@RestController
@RequestMapping("/service")
public class ApplicationController {

    /**
     * コンストラクタ
     *
     */
    public ApplicationController() {
    }

    @Autowired
    CustomRepository custRepo;

    @Operation(summary = "Returns 500m mesh census data", 
            description = "Returns geojson features of 500m mesh data containing Census Information that overlaps input geojson polygon",
            parameters = {
                    @Parameter(name = "geoJson", description = "GeoJSON Polygon that will be used to query the mesh census data", required = true)
                    }

            )
    @GetMapping(value = "/census/mesh4", produces = "application/json;charset=UTF-8")
            public String getMeshData(@RequestParam("geoJson") String geoJson) {
        return custRepo.queryMesh4(geoJson);
    }

    @Operation(summary = "Returns POIs", description = "Returns geojson features of POI data that is contained in the input geojson polygon", parameters = {
            @Parameter(name = "geoJson", description = "GeoJSON Polygon that will be used to query the POIs", required = true)
    }
    )
    @GetMapping(value = "/poi", produces = "application/json;charset=UTF-8")
    public String getPoiData(@RequestParam("geoJson") String geoJson) {
        return custRepo.queryPoi(geoJson);
    }
}
