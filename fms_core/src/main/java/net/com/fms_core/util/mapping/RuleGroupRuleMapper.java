/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.util.mapping;
import net.com.fms_core.dto.RuleGroupRuleDTO;
import net.com.fms_core.entity.RuleGroupRule;
import net.com.fms_core.util.DateTime;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.Date;
import java.util.Collection;

@Mapper(componentModel = "spring", uses = {FmsRuleMapper.class})
public interface RuleGroupRuleMapper {
    FmsRuleMapper FmsRuleMapper = Mappers.getMapper(FmsRuleMapper.class);
    @Named("mapBasicRuleGroupRuleDTOToRuleGroupRuleForSave")
    @Mapping(target = "ruleGroupRuleId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "updatedAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "fmsRuleId.fmsRuleId", source = "fmsRuleId")
    @Mapping(target = "ruleGroupId.ruleGroupId", source = "ruleGroupId")
    RuleGroupRule mapBasicRuleGroupRuleDTOToRuleGroupRuleForSave(RuleGroupRuleDTO ruleGroupRuleDTO);
    @Named("mapBasicRuleGroupRuleDTOCollectionToRuleGroupRuleCollectionForRuleGroupSave")
    @IterableMapping(qualifiedByName = "mapBasicRuleGroupRuleDTOToRuleGroupRuleForSave")
    Collection<RuleGroupRule> mapBasicRuleGroupRuleDTOCollectionToRuleGroupRuleCollectionForRuleGroupSave(Collection<RuleGroupRuleDTO> ruleGroupRuleDTOs);
    @Named("mapBasicRuleGroupRuleDTOToRuleGroupRuleForUpdate")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "updatedAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "fmsRuleId.fmsRuleId", source = "fmsRuleId")
    @Mapping(target = "ruleGroupId.ruleGroupId", source = "ruleGroupId")
    RuleGroupRule mapBasicRuleGroupRuleDTOToRuleGroupRuleForUpdate(RuleGroupRuleDTO ruleGroupRuleDTO);
    @Named("mapBasicRuleGroupRuleDTOCollectionToRuleGroupRuleCollectionForRuleGroupUpdate")
    @IterableMapping(qualifiedByName = "mapBasicRuleGroupRuleDTOToRuleGroupRuleForUpdate")
    Collection<RuleGroupRule> mapBasicRuleGroupRuleDTOCollectionToRuleGroupRuleCollectionForRuleGroupUpdate(Collection<RuleGroupRuleDTO> ruleGroupRuleDTOs);
    @Named("mapBasicRuleGroupRuleToRuleGroupRuleDTOForGet")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "fmsRuleId", source = "fmsRuleId.fmsRuleId")
    @Mapping(target = "FmsRule", source = "fmsRuleId", qualifiedByName = "mapFmsRuleToFmsRuleDTOForGetWithConditions")
    @Mapping(target = "ruleGroupId", source = "ruleGroupId.ruleGroupId")
    RuleGroupRuleDTO mapBasicRuleGroupRuleToRuleGroupRuleDTOForGet(RuleGroupRule ruleGroupRuleDTO);
    @Named("mapBasicRuleGroupRuleCollectionToRuleGroupRuleDTOCollectionForRuleGroupGet")
    @IterableMapping(qualifiedByName = "mapBasicRuleGroupRuleToRuleGroupRuleDTOForGet")
    Collection<RuleGroupRuleDTO> mapBasicRuleGroupRuleCollectionToRuleGroupRuleDTOCollectionForRuleGroupGet(Collection<RuleGroupRule> ruleGroupRules);
    default Date getCurrentTimestamp() {
        return DateTime.getCurrentTimestamp();
    }
}
