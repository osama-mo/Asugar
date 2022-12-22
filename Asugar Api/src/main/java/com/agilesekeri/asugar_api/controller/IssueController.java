package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.service.AppUserService;
import com.agilesekeri.asugar_api.service.ProjectService;
import com.agilesekeri.asugar_api.service.IssueService;
import com.agilesekeri.asugar_api.model.request.IssueCreateRequest;
import com.agilesekeri.asugar_api.model.request.IssueGenericUpdateRequest;
import com.agilesekeri.asugar_api.model.entity.IssueEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user/issue")
@RequiredArgsConstructor
@Getter
@Setter
public class IssueController {
    private final AppUserService appUserService;
    private final IssueService issueService;
    private final ProjectService projectService;

    // TODO: Gotta return the DTO's at POST/PUT/PATCH requests.
    @PostMapping("/create")
    public void createIssue(@RequestBody IssueCreateRequest issueCreateRequest) {
        issueService.createIssue(issueCreateRequest);
    }

    // TODO: Need DTO's for GET requests.
    @GetMapping("/{issueId}")
    public IssueEntity getIssue(@PathVariable Long issueId) {
        return issueService.getIssue(issueId);
    }

    // I may need user to login aswell
    @GetMapping("/info/{issueId}")
    public String viewInfo(@PathVariable Long issueId) {
        return issueService.viewInfo(issueId);
    }

//    @PatchMapping("/update-condition/{issueId}/{conditionType}")
//    public void updateCondition(@PathVariable Long issueId, @PathVariable("conditionType") TaskConditionEnum taskConditionEnum) {
//        issueService.updateCondition(issueId, taskConditionEnum);
//    }

//    @PutMapping("/update-plannedTo/{issueId}/{localDateTime}")
//    public void updatePlannedTo(@PathVariable Long issueId, @PathVariable("localDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime) {
//        issueService.updatePlannedTo(issueId, localDateTime);
//    }

//    @PutMapping("/assignTo/{issueId}")
//    public void assignToUser(@PathVariable("issueId") Long issueId, @RequestParam("username") String username){
//        issueService.assignToUser(issueId, username);
//    }

    // TODO: Below I'll be combining all of the set/edit methods which some are written above.

    @PatchMapping("/update/{issueId}")
    public void updateIssue(@PathVariable("issueId") Long issueId, @RequestBody IssueGenericUpdateRequest issueGenericUpdateRequest){
        issueService.updateIssue(issueId, issueGenericUpdateRequest);
    }

}
