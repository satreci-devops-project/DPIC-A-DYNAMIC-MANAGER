package dpic.dynamicmanager.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DynamicAnalysisResultRequestDTO {
    private String jobName;
    private Double branchCoverage;
    private Double ClassCoverage;
    private Double ComplexityScore;
    private Double InstructionCoverage;
    private Double LineCoverage;
    private Double MethodCoverage;
}
