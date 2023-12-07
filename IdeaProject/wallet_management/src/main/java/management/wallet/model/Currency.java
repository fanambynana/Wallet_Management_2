package management.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Currency {
    private int id;
    private String name;
    private String code;
}
