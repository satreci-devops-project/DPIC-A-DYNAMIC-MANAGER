package dpic.dynamicmanager.manager;

import dpic.dynamicmanager.manager.dto.DynamicAnalysisResultRequestDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class DynamicAnalysisResultRepository {
    private final JdbcTemplate template;

    public DynamicAnalysisResultRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public void save(DynamicAnalysisResultRequestDTO dynamicAnalysisResultRequestDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into DYNAMIC_ANALYSIS_RESULT" +
                "(JOB_NAME, BRANCH_COVERAGE, CLASS_COVERAGE, COMPLEXITY_SCORE, INSTRUCTION_COVERAGE, LINE_COVERAGE, METHOD_COVERAGE) " +
                "values" +
                "(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, dynamicAnalysisResultRequestDTO.getJobName());
            preparedStatement.setDouble(2, dynamicAnalysisResultRequestDTO.getBranchCoverage());
            preparedStatement.setDouble(3, dynamicAnalysisResultRequestDTO.getClassCoverage());
            preparedStatement.setDouble(4, dynamicAnalysisResultRequestDTO.getComplexityScore());
            preparedStatement.setDouble(5, dynamicAnalysisResultRequestDTO.getInstructionCoverage());
            preparedStatement.setDouble(6, dynamicAnalysisResultRequestDTO.getLineCoverage());
            preparedStatement.setDouble(7, dynamicAnalysisResultRequestDTO.getMethodCoverage());
            return preparedStatement;
        };
        template.update(preparedStatementCreator, keyHolder);

        //return (Integer) keyHolder.getKeys().get("ID");
    }

}
