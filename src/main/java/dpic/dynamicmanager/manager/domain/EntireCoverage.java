package dpic.dynamicmanager.manager.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EntireCoverage {
    private Coverage branchCoverage;
    private Coverage classCoverage;
    private Coverage complexityScore;
    private Coverage instructionCoverage;
    private Coverage lineCoverage;
    private Coverage methodCoverage;
}
