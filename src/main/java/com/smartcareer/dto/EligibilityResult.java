package com.smartcareer.dto;

import java.util.ArrayList;
import java.util.List;

public class EligibilityResult {

    private boolean eligible;

    private boolean cgpaPassed;
    private boolean skillsPassed;
    private boolean backlogsPassed;

    private Double studentCgpa;
    private Double requiredCgpa;

    private Integer studentBacklogs;

    private List<String> matchingSkills = new ArrayList<>();
    private List<String> missingSkills = new ArrayList<>();
    private List<String> reasons = new ArrayList<>();

    public EligibilityResult() {
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public boolean isCgpaPassed() {
        return cgpaPassed;
    }

    public void setCgpaPassed(boolean cgpaPassed) {
        this.cgpaPassed = cgpaPassed;
    }

    public boolean isSkillsPassed() {
        return skillsPassed;
    }

    public void setSkillsPassed(boolean skillsPassed) {
        this.skillsPassed = skillsPassed;
    }

    public boolean isBacklogsPassed() {
        return backlogsPassed;
    }

    public void setBacklogsPassed(boolean backlogsPassed) {
        this.backlogsPassed = backlogsPassed;
    }

    public Double getStudentCgpa() {
        return studentCgpa;
    }

    public void setStudentCgpa(Double studentCgpa) {
        this.studentCgpa = studentCgpa;
    }

    public Double getRequiredCgpa() {
        return requiredCgpa;
    }

    public void setRequiredCgpa(Double requiredCgpa) {
        this.requiredCgpa = requiredCgpa;
    }

    public Integer getStudentBacklogs() {
        return studentBacklogs;
    }

    public void setStudentBacklogs(Integer studentBacklogs) {
        this.studentBacklogs = studentBacklogs;
    }

    public List<String> getMatchingSkills() {
        return matchingSkills;
    }

    public void setMatchingSkills(List<String> matchingSkills) {
        this.matchingSkills = matchingSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}