package br.com.technow.springsmartspecification.jpa.query;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JpaSpecificationMeta {

    private List<JpaSpecificationField> fields;

}
