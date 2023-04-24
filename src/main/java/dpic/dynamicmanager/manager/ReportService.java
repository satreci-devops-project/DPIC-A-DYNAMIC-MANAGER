package dpic.dynamicmanager.manager;

import dpic.dynamicmanager.manager.domain.Coverage;
import dpic.dynamicmanager.manager.domain.EntireCoverage;
import dpic.dynamicmanager.manager.dto.DynamicAnalysisResultRequestDTO;
import dpic.dynamicmanager.manager.dto.RegisterBuildRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    @Value("${jenkins.api.server.jacoco-prefix-url}")
    private String coveragePrefixUrl;

    @Value("${jenkins.api.server.jacoco-postfix-url}")
    private String coveragePostfixUrl;

    @Value("${jenkins.api.server.user}")
    private String user;

    @Value("${jenkins.api.server.user-token}")
    private String token;

    private final DynamicAnalysisResultRepository dynamicAnalysisResultRepository;
    private final String [] coverageType = {
            "branchCoverage",
            "classCoverage",
            "complexityScore",
            "instructionCoverage",
            "lineCoverage",
            "methodCoverage"
    };

    private final String [] coverageInfo = {
            "covered",
            "missed",
            "percentage",
            "percentageFloat",
            "total"
    };

    public void reportCoverage(RegisterBuildRequestDTO registerBuildRequestDTO) {
        EntireCoverage entireCoverage = readCoverageJson(registerBuildRequestDTO);
        DynamicAnalysisResultRequestDTO dynamicAnalysisResultRequestDTO = dynamicAnalysisResultRequestDTOMapper(
                entireCoverage,
                registerBuildRequestDTO
        );
        dynamicAnalysisResultRepository.save(dynamicAnalysisResultRequestDTO);
    }

    public DynamicAnalysisResultRequestDTO dynamicAnalysisResultRequestDTOMapper(
            EntireCoverage entireCoverage,
            RegisterBuildRequestDTO registerBuildRequestDTO
    ) {
        DynamicAnalysisResultRequestDTO dynamicAnalysisResultRequestDTO = new DynamicAnalysisResultRequestDTO(
                        registerBuildRequestDTO.getJobName(),
                Math.round(entireCoverage.getBranchCoverage().getPercentageFloat()*100)/100.0,
                Math.round(entireCoverage.getClassCoverage().getPercentageFloat()*100)/100.0,
                Math.round(entireCoverage.getComplexityScore().getPercentageFloat()*100)/100.0,
                Math.round(entireCoverage.getInstructionCoverage().getPercentageFloat()*100)/100.0,
                Math.round(entireCoverage.getLineCoverage().getPercentageFloat()*100)/100.0,
                Math.round(entireCoverage.getMethodCoverage().getPercentageFloat()*100)/100.0
        );

        return dynamicAnalysisResultRequestDTO;
    }

    public String makeCoverageUrl(RegisterBuildRequestDTO registerBuildRequestDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append(coveragePrefixUrl);
        sb.append(registerBuildRequestDTO.getJobName());
        sb.append('/');
        sb.append(registerBuildRequestDTO.getBuildNo());
        sb.append(coveragePostfixUrl);
        return sb.toString();
    }

    public EntireCoverage readCoverageJson(RegisterBuildRequestDTO registerBuildRequestDTO) {
        EntireCoverage entireCoverage = null;
        try {
            String coverageUrl = makeCoverageUrl(registerBuildRequestDTO);
            URL url = new URL(coverageUrl);
            String authStr = user + ":" + token;
            String encoding = Base64.getEncoder().encodeToString(authStr.getBytes("utf-8"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            InputStream content = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            entireCoverage = new EntireCoverage();
            List<String> totalLine = new ArrayList();
            String line;
            while ((line = in.readLine()) != null) {
                totalLine.add(line);
            }
            parseCoverageJson(totalLine, entireCoverage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entireCoverage;
    }

    public void parseCoverageJson(List<String> totalLine, EntireCoverage entireCoverage) {
        Coverage coverage = null;
        String currentReportType = null;
        for(int i=0 ; i<totalLine.size() ; i++) {
            String line = totalLine.get(i);
            String [] splitBlankLine = line.strip().split(" ");
            if(splitBlankLine.length > 1) {
                String key = splitBlankLine[0].substring(1, splitBlankLine[0].length()-1);
                String value = splitBlankLine[2];
                if(value.charAt(value.length()-1) == ',') {
                    value = value.substring(0, value.length()-1);
                }
                if(Arrays.asList(coverageType).contains(key)) {
                    coverage = new Coverage();
                    coverage.setType(key);
                    currentReportType = key;
                }
                if(key.equals("covered")) coverage.setCovered(Integer.parseInt(value));
                if(key.equals("missed")) coverage.setMissed(Integer.parseInt(value));
                if(key.equals("percentage")) coverage.setPercentage(Integer.parseInt(value));
                if(key.equals("percentageFloat")) coverage.setPercentageFloat(Double.parseDouble(value));
                if(key.equals("total")) {
                    coverage.setTotal(Integer.parseInt(value));
                    if(currentReportType.equals("branchCoverage")) entireCoverage.setBranchCoverage(coverage);
                    if(currentReportType.equals("classCoverage")) entireCoverage.setClassCoverage(coverage);
                    if(currentReportType.equals("complexityScore")) entireCoverage.setComplexityScore(coverage);
                    if(currentReportType.equals("instructionCoverage")) entireCoverage.setInstructionCoverage(coverage);
                    if(currentReportType.equals("lineCoverage")) entireCoverage.setLineCoverage(coverage);
                    if(currentReportType.equals("methodCoverage")) entireCoverage.setMethodCoverage(coverage);
                }
            }
        }
    }
}
