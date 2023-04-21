package dpic.dynamicmanager.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterBuildRequestDTO {
    private String jobName;
    private int buildNo;
}
