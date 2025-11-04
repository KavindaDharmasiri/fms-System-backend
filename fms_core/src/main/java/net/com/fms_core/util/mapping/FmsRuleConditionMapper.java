/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.util.mapping;
import net.com.fms_core.dto.FmsRuleConditionDTO;
import net.com.fms_core.entity.FmsRuleCondition;
import net.com.fms_core.util.DateTime;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.Collection;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface FmsRuleConditionMapper {
    @Named("FmsRuleConditionDTOToFmsRuleCondition")
    @Mapping(target = "fmsElementId.fmsElementId",  source = "fmsElementId")
    @Mapping(target = "fmsRuleId.fmsRuleId", source = "fmsRuleId")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "updatedAt",  expression = "java(getCurrentTimestamp())")
    FmsRuleCondition FmsRuleConditionDTOToFmsRuleCondition(FmsRuleConditionDTO FmsRuleConditionDTO);
    @Named("basicFmsRuleConditionDTOCollectionTofmsRuleConditionCollection")
    @IterableMapping(qualifiedByName = "FmsRuleConditionDTOToFmsRuleCondition")
    Collection<FmsRuleCondition> basicFmsRuleConditionDTOCollectionTofmsRuleConditionCollection(
            Collection<FmsRuleConditionDTO> FmsRuleConditionDTOCollection);
    @Named("FmsRuleConditionToFmsRuleConditionDTO")
    @Mapping(target = "fmsElementId",  source = "fmsElementId.fmsElementId")
    @Mapping(target = "FmsElementOperator", source = "fmsElementId.operator")
    @Mapping(target = "FmsElementValue", source = "fmsElementId.value")
    @Mapping(target = "FmsElement",  ignore = true)
    @Mapping(target = "fmsRuleId", source = "fmsRuleId.fmsRuleId")
    @Mapping(target = "FmsRule", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "updatedAt",  expression = "java(getCurrentTimestamp())")
    FmsRuleConditionDTO FmsRuleConditionToFmsRuleConditionDTO(FmsRuleCondition FmsRuleCondition);
    @Named("basicfmsRuleConditionCollectionToFmsRuleConditionDTOCollection")
    @IterableMapping(qualifiedByName = "FmsRuleConditionToFmsRuleConditionDTO")
    Collection<FmsRuleConditionDTO> basicfmsRuleConditionCollectionToFmsRuleConditionDTOCollection(
            Collection<FmsRuleCondition> FmsRuleConditionDTOCollection);
    default Date getCurrentTimestamp() {
        return DateTime.getCurrentTimestamp();
    }
}
