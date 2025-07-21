package GuiBSantos.TaskManager.mapper;

import GuiBSantos.TaskManager.dto.CompanyDTO;
import GuiBSantos.TaskManager.model.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyDTO toDTO(Company company);

    Company toEntity(CompanyDTO dto);
}