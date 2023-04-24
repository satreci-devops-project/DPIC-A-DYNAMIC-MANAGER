package dpic.dynamicmanager.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Coverage {
    private String type;
    private int covered;
    private int missed;
    private int percentage;
    private double percentageFloat;
    private int total;
}
