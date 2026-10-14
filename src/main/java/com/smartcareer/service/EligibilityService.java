package com.smartcareer.service;

import com.smartcareer.dto.EligibilityResult;
import com.smartcareer.entity.Job;
import com.smartcareer.entity.Skill;
import com.smartcareer.entity.Student;
import com.smartcareer.repository.JobRepository;
import com.smartcareer.repository.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
// import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EligibilityService {

    private final JobRepository jobRepository;
    private final StudentRepository studentRepository;

    public EligibilityService(
            JobRepository jobRepository,
            StudentRepository studentRepository) {

        this.jobRepository = jobRepository;
        this.studentRepository = studentRepository;
    }

    public EligibilityResult checkEligibility(
            Long jobId,
            Long studentId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found with ID: " + jobId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found with ID: " + studentId));

        EligibilityResult result = new EligibilityResult();

        /*
         * ------------------------------------------------
         * 1. CGPA CHECK
         * ------------------------------------------------
         */

        result.setStudentCgpa(student.getCgpa());
        result.setRequiredCgpa(job.getMinCgpa());

        if (job.getMinCgpa() == null) {

            result.setCgpaPassed(true);

            result.getReasons().add(
                    "No minimum CGPA requirement specified."
            );

        } else if (student.getCgpa() == null) {

            result.setCgpaPassed(false);

            result.getReasons().add(
                    "Student CGPA is not available."
            );

        } else if (student.getCgpa() >= job.getMinCgpa()) {

            result.setCgpaPassed(true);

            result.getReasons().add(
                    "CGPA requirement satisfied."
            );

        } else {

            result.setCgpaPassed(false);

            result.getReasons().add(
                    "CGPA is below the required minimum."
            );
        }


        /*
         * ------------------------------------------------
         * 2. SKILLS CHECK
         * ------------------------------------------------
         */

        Set<String> studentSkills = student.getSkills()
                .stream()
                .map(Skill::getName)
                .filter(name -> name != null && !name.trim().isEmpty())
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        if (job.getRequiredSkills() == null
                || job.getRequiredSkills().trim().isEmpty()) {

            result.setSkillsPassed(true);

            result.getReasons().add(
                    "No specific skills are required for this job."
            );

        } else {

            Set<String> requiredSkills = Arrays.stream(
                            job.getRequiredSkills().split(",")
                    )
                    .map(String::trim)
                    .filter(skill -> !skill.isEmpty())
                    .map(String::toLowerCase)
                    .collect(Collectors.toCollection(HashSet::new));

            for (String requiredSkill : requiredSkills) {

                if (studentSkills.contains(requiredSkill)) {

                    result.getMatchingSkills().add(
                            capitalizeSkill(requiredSkill)
                    );

                } else {

                    result.getMissingSkills().add(
                            capitalizeSkill(requiredSkill)
                    );
                }
            }

            boolean allSkillsMatched =
                    result.getMissingSkills().isEmpty();

            result.setSkillsPassed(allSkillsMatched);

            if (allSkillsMatched) {

                result.getReasons().add(
                        "All required skills are available."
                );

            } else {

                result.getReasons().add(
                        "One or more required skills are missing."
                );
            }
        }


        /*
         * ------------------------------------------------
         * 3. BACKLOG CHECK
         * ------------------------------------------------
         *
         * Current Job entity does not contain a maximum
         * backlog field.
         *
         * Therefore, current project rule:
         * Student must have 0 backlogs.
         */

        result.setStudentBacklogs(student.getBacklogs());

        if (student.getBacklogs() == null) {

            result.setBacklogsPassed(false);

            result.getReasons().add(
                    "Backlog information is not available."
            );

        } else if (student.getBacklogs() == 0) {

            result.setBacklogsPassed(true);

            result.getReasons().add(
                    "Backlog requirement satisfied."
            );

        } else {

            result.setBacklogsPassed(false);

            result.getReasons().add(
                    "Student has active backlogs."
            );
        }


        /*
         * ------------------------------------------------
         * 4. FINAL ELIGIBILITY
         * ------------------------------------------------
         */

        boolean finalEligibility =
                result.isCgpaPassed()
                        && result.isSkillsPassed()
                        && result.isBacklogsPassed();

        result.setEligible(finalEligibility);

        return result;
    }


    /*
     * Converts:
     *
     * spring boot
     *
     * into:
     *
     * Spring boot
     */
    private String capitalizeSkill(String skill) {

        if (skill == null || skill.isEmpty()) {
            return skill;
        }

        return Character.toUpperCase(skill.charAt(0))
                + skill.substring(1);
    }
}