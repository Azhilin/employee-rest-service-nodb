package alex.enterprise.employeenodb.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class Permission {
    @NonNull
    private String url;
    @NonNull
    private Boolean flag;
}
