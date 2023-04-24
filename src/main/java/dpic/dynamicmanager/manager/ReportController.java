package dpic.dynamicmanager.manager;

import dpic.dynamicmanager.manager.dto.RegisterBuildRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/builds")
    public void requestRegisterBuild(@RequestBody RegisterBuildRequestDTO registerBuildRequestDTO) {
        log.info("RegisterBuild ::: jobName={}, buildNo={}",
                registerBuildRequestDTO.getJobName(),
                registerBuildRequestDTO.getBuildNo()
        );
        reportService.reportCoverage(registerBuildRequestDTO);
    }
}