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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

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

    @Operation(summary = "Returns 500m mesh census data (GET)", description = "Returns geojson features of 500m mesh data containing Census Information that overlaps input geojson polygon (GET)", parameters = {
            @Parameter(name = "geoJson", description = "GeoJSON Polygon that will be used to query the mesh census data", required = true)
    })
    @GetMapping(value = "/census/mesh4", produces = "application/json;charset=UTF-8")
    public String getMeshDataGet(@RequestParam("geoJson") String geoJson) {
        return custRepo.queryMesh4(geoJson);
    }

    @Operation(summary = "Returns 500m mesh census data (POST)", description = "Returns geojson features of 500m mesh data containing Census Information that overlaps input geojson polygon (POST)", parameters = {
    })
    @PostMapping(value = "/census/mesh4", produces = "application/json;charset=UTF-8")
    public String getMeshDataPost(@RequestBody String geoJson) {
        return custRepo.queryMesh4(geoJson);
    }

    @Operation(summary = "Returns POIs", description = "Returns geojson features of POI data that is contained in the input geojson polygon", parameters = {
            @Parameter(name = "geoJson", description = "GeoJSON Polygon that will be used to query the POIs", required = true)
    })
    @GetMapping(value = "/poi", produces = "application/json;charset=UTF-8")
    public String getPoiData(@RequestParam("geoJson") String geoJson) {
        return custRepo.queryPoi(geoJson);
    }

    @Operation(summary = "Returns People Flow (人流) Information", description = "Returns JSON People Information that is within the input geojson polygon", parameters = {
            @Parameter(name = "geoJson", description = "GeoJSON Polygon that will be used to query the People Flow data", required = true)
    })
    @GetMapping(value = "/people_flow", produces = "application/json;charset=UTF-8")
    public String getPeopleFlowData(@RequestParam("geoJson") String geoJson) {

        String retVal = new String();

        try {
            String sex = custRepo.queryPeopleFlow(geoJson, "sex");
            String job = custRepo.queryPeopleFlow(geoJson, "job");
            String position = custRepo.queryPeopleFlow(geoJson, "position");
            String address = custRepo.queryPeopleFlow(geoJson, "address");
            String maritalstatus = custRepo.queryPeopleFlow(geoJson, "maritalstatus");
            String residencestatus = custRepo.queryPeopleFlow(geoJson, "residencestatus");
            String recentacademichistory = custRepo.queryPeopleFlow(geoJson, "recentacademichistory");
            String personalincome = custRepo.queryPeopleFlow(geoJson, "personalincome");
            String householdincome = custRepo.queryPeopleFlow(geoJson, "householdincome");

            retVal = String.format("""
                    {
                     "people_flow_information" : {
                         "sex" : %s,
                         "job" : %s,
                         "position": %s,
                         "address": %s,
                         "marital_status": %s,
                         "residence_status": %s,
                         "recent_academic_history": %s,
                         "personal_income": %s,
                         "household_income": %s
                     }
                    }
                    """, sex, job, position, address, maritalstatus,
                    residencestatus, recentacademichistory,
                    personalincome, householdincome);

        } catch (Exception e) {
            return "{}";
        }

        return retVal;
    }
}
