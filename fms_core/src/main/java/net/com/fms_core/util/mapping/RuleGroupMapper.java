/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.util.mapping;
import net.com.fms_core.dto.RuleGroupDTO;
import net.com.fms_core.entity.PaymentNetwork;
import net.com.fms_core.entity.ReactionTemplate;
import net.com.fms_core.entity.RuleGroup;
import net.com.fms_core.util.DateTime;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring", uses = {RuleGroupRoleMapper.class, RuleGroupRuleMapper.class})
public interface RuleGroupMapper {
    RuleGroupRoleMapper ruleGroupRoleMapper = Mappers.getMapper(RuleGroupRoleMapper.class);
    RuleGroupRuleMapper ruleGroupRuleMapper = Mappers.getMapper(RuleGroupRuleMapper.class);
    @Mapping(target = "ruleGroupId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "updatedAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "transactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "ruleGroupRuleCollection", source = "ruleGroupRuleCollection", qualifiedByName = "mapBasicRuleGroupRuleDTOCollectionToRuleGroupRuleCollectionForRuleGroupSave")
    @Mapping(target = "ruleGroupRoleCollection", source = "ruleGroupRoleCollection", qualifiedByName = "mapBasicRuleGroupRoleDTOCollectionToRuleGroupRoleCollectionForRuleGroupSave")
    @Mapping(target = "testTransactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "paymentNetworkId.paymentNetworkId", source = "paymentNetworkId")
    @Mapping(target = "reactionTemplateId.reactionTemplateId", source = "reactionTemplateId")
    RuleGroup mapRuleGroupDTOToRuleGroupForSave(RuleGroupDTO ruleGroupDTO);
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "updatedAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "transactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "ruleGroupRuleCollection", source = "ruleGroupRuleCollection", qualifiedByName = "mapBasicRuleGroupRuleDTOCollectionToRuleGroupRuleCollectionForRuleGroupUpdate")
    @Mapping(target = "ruleGroupRoleCollection", source = "ruleGroupRoleCollection", qualifiedByName = "mapBasicRuleGroupRoleDTOCollectionToRuleGroupRoleCollectionForRuleGroupUpdate")
    @Mapping(target = "testTransactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "paymentNetworkId", source = "paymentNetworkId", qualifiedByName = "mapPaymentNetworkId")
    @Mapping(target = "reactionTemplateId", source = "reactionTemplateId", qualifiedByName = "mapReactionTemplateId")
    RuleGroup mapRuleGroupDTOToRuleGroupForUpdate(RuleGroupDTO ruleGroupDTO, @MappingTarget RuleGroup ruleGroup);
    @Named("mapReactionTemplateId")
    default ReactionTemplate mapReactionTemplateId(Integer id) {
        if (id == null) return null;
        ReactionTemplate rt = new ReactionTemplate();
        rt.setReactionTemplateId(id);
        return rt;
    }
    @Named("mapPaymentNetworkId")
    default PaymentNetwork mapPaymentNetworkId(Integer id) {
        if (id == null) return null;
        PaymentNetwork pn = new PaymentNetwork();
        pn.setPaymentNetworkId(id);
        return pn;
    }
    @Named("mapBasicRuleGroupToRuleGroupDTOForGet")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "updatedAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "transactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "ruleGroupRuleCollection", ignore = true)
    @Mapping(target = "ruleGroupRoleCollection", ignore = true)
    @Mapping(target = "testTransactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "paymentNetworkId", source = "paymentNetworkId.paymentNetworkId")
    @Mapping(target = "paymentNetwork", source = "paymentNetworkId")
    @Mapping(target = "paymentNetwork.createdBy", ignore = true)
    @Mapping(target = "paymentNetwork.updatedBy", ignore = true)
    @Mapping(target = "paymentNetwork.FmsRuleCollection", ignore = true)
    @Mapping(target = "paymentNetwork.ruleGroupCollection", ignore = true)
    @Mapping(target = "reactionTemplateId", source = "reactionTemplateId.reactionTemplateId")
    @Mapping(target = "ruleCount", expression = "java(ruleGroup.getRuleGroupRuleCollection() != null ? ruleGroup.getRuleGroupRuleCollection().size() : 0)")
    RuleGroupDTO mapBasicRuleGroupToRuleGroupDTOForGet(RuleGroup ruleGroup);
    @IterableMapping(qualifiedByName = "mapBasicRuleGroupToRuleGroupDTOForGet")
    List<RuleGroupDTO> mapBasicRuleGroupListToRuleGroupDTOListForGet(List<RuleGroup> ruleGroups);
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "transactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "ruleGroupRuleCollection", source = "ruleGroupRuleCollection", qualifiedByName = "mapBasicRuleGroupRuleCollectionToRuleGroupRuleDTOCollectionForRuleGroupGet")
    @Mapping(target = "ruleGroupRoleCollection", source = "ruleGroupRoleCollection", qualifiedByName = "mapBasicRuleGroupRoleCollectionToRuleGroupRoleDTOCollectionForRuleGroupGet")
    @Mapping(target = "testTransactionFlaggedRulesCollection", ignore = true)
    @Mapping(target = "paymentNetworkId", source = "paymentNetworkId.paymentNetworkId")
    @Mapping(target = "paymentNetwork", source = "paymentNetworkId")
    @Mapping(target = "paymentNetwork.createdBy", ignore = true)
    @Mapping(target = "paymentNetwork.updatedBy", ignore = true)
    @Mapping(target = "paymentNetwork.FmsRuleCollection", ignore = true)
    @Mapping(target = "paymentNetwork.ruleGroupCollection", ignore = true)
    @Mapping(target = "reactionTemplateId", source = "reactionTemplateId.reactionTemplateId")
    @Mapping(target = "reactionTemplate", source = "reactionTemplateId")
    @Mapping(target = "reactionTemplate.ruleGroupCollection", ignore = true)
    @Mapping(target = "reactionTemplate.reactionTemplateRoleCollection", ignore = true)
    RuleGroupDTO mapBasicRuleGroupToRuleGroupDTOForGetIndividual(RuleGroup ruleGroup);
    default Page<RuleGroupDTO> mapBasicBranchPageToBranchDTOPageForGet(Page<RuleGroup> ruleGroups) {
        List<RuleGroupDTO> ruleGroupsDTOs = mapBasicRuleGroupListToRuleGroupDTOListForGet(ruleGroups.getContent());
        return new PageImpl<>(ruleGroupsDTOs, ruleGroups.getPageable(), ruleGroups.getTotalElements());
    }
    default Date getCurrentTimestamp() {
        return DateTime.getCurrentTimestamp();
    }
}
