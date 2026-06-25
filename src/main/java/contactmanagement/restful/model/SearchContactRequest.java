package contactmanagement.restful.model;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchContactRequest {

    private String name;

    private String email;

    private String phone;

    @NonNull
    private Integer page;

    @NonNull
    private Integer size;
}
