package dpic.dynamicmanager.manager;

import dpic.dynamicmanager.manager.dto.RegisterBuildRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Service
public class ReportService {

    @Value("${jenkins.api.server.jacoco-prefix-url}")
    private String coveragePrefixUrl;

    @Value("${jenkins.api.server.jacoco-postfix-url}")
    private String coveragePostfixUrl;

    @Value("${jenkins.api.server.user}")
    private String user;

    @Value("${jenkins.api.server.user-token}")
    private String token;

    public String makeCoverageUrl(RegisterBuildRequestDTO registerBuildRequestDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append(coveragePrefixUrl);
        sb.append(registerBuildRequestDTO.getJobName());
        sb.append('/');
        sb.append(registerBuildRequestDTO.getBuildNo());
        sb.append(coveragePostfixUrl);
        return sb.toString();
    }

    public void readJacocoJson(RegisterBuildRequestDTO registerBuildRequestDTO) {
        try {
            String coverageUrl = makeCoverageUrl(registerBuildRequestDTO);
            URL url = new URL(coverageUrl);
            String authStr = user + ":" + token;
            String encoding = Base64.getEncoder().encodeToString(authStr.getBytes("utf-8"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            InputStream content = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseJacocoJson() {

    }

}
