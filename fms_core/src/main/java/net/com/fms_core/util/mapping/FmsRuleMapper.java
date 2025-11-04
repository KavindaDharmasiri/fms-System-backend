/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.util.mapping;
import net.com.fms_core.dto.FmsRuleConditionDTO;
import net.com.fms_core.dto.FmsRuleDTO;
import net.com.fms_core.entity.FmsRule;
import net.com.fms_core.entity.FmsRuleCondition;
import net.com.fms_core.util.DateTime;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring", uses = {FmsRuleConditionMapper.class})
public interface FmsRuleMapper {
    FmsRuleConditionMapper FmsRuleConditionMapper = Mappers.getMapper(FmsRuleConditionMapper.class);
    @Mapping(target = "fmsRuleId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "updatedAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "fmsRuleConditionCollection", source = "fmsRuleConditionCollection", qualifiedByName = "basicFmsRuleConditionDTOCollectionTofmsRuleConditionCollection")
    @Mapping(target = "transactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "paymentNetworkId.paymentNetworkId", source = "paymentNetworkId")
    @Mapping(target = "ruleGroupRuleCollection", ignore = true)
    @Mapping(target = "testTransactionFlaggedRulesCollection", ignore = true)
    FmsRule mapFmsRuleDTOToFmsRuleForSave(FmsRuleDTO FmsRuleDTO);
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "updatedAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "fmsRuleConditionCollection", source = "fmsRuleConditionCollection", qualifiedByName = "basicFmsRuleConditionDTOCollectionTofmsRuleConditionCollection")
    @Mapping(target = "transactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "paymentNetworkId.paymentNetworkId", source = "paymentNetworkId")
    @Mapping(target = "ruleGroupRuleCollection", ignore = true)
    @Mapping(target = "testTransactionFlaggedRulesCollection", ignore = true)
    FmsRule mapFmsRuleDTOToFmsRuleForUpdate(FmsRuleDTO FmsRuleDTO, @MappingTarget FmsRule FmsRule);
    @Named("mapFmsRuleToFmsRuleDTOForGetWithConditions")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  ignore = true)
    @Mapping(target = "updatedAt",  ignore = true)
    @Mapping(target = "fmsRuleConditionCollection", source = "fmsRuleConditionCollection", qualifiedByName = "basicfmsRuleConditionCollectionToFmsRuleConditionDTOCollection")
    @Mapping(target = "transactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "paymentNetworkId", source = "paymentNetworkId.paymentNetworkId")
    @Mapping(target = "ruleGroupRuleCollection", ignore = true)
    @Mapping(target = "testTransactionFlaggedRulesCollection", ignore = true)
    FmsRuleDTO mapFmsRuleToFmsRuleDTOForGetWithConditions(FmsRule FmsRule);
    @Named("mapBasicFmsRuleToFmsRuleDTOForGet")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  ignore = true)
    @Mapping(target = "updatedAt",  ignore = true)
    @Mapping(target = "fmsRuleConditionCollection", ignore = true)
    @Mapping(target = "transactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "paymentNetworkId", source = "paymentNetworkId.paymentNetworkId")
    @Mapping(target = "ruleGroupRuleCollection", ignore = true)
    @Mapping(target = "testTransactionFlaggedRulesCollection", ignore = true)
    FmsRuleDTO mapBasicFmsRuleToFmsRuleDTOForGet(FmsRule FmsRule);
    @IterableMapping(qualifiedByName = "mapBasicFmsRuleToFmsRuleDTOForGet")
    List<FmsRuleDTO> mapBasicFmsRuleListToFmsRuleDTOListForGet(List<FmsRule> FmsRules);
    default Page<FmsRuleDTO> mapBasicBranchPageToBranchDTOPageForGet(Page<FmsRule> FmsRules) {
        List<FmsRuleDTO> FmsRulesDTOs = mapBasicFmsRuleListToFmsRuleDTOListForGet(FmsRules.getContent());
        return new PageImpl<>(FmsRulesDTOs, FmsRules.getPageable(), FmsRules.getTotalElements());
    }
    default Date getCurrentTimestamp() {
        return DateTime.getCurrentTimestamp();
    }
}
