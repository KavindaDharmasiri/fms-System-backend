/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.util.mapping;
import net.com.fms_core.dto.RuleGroupRoleDTO;
import net.com.fms_core.entity.RuleGroupRole;
import net.com.fms_core.util.DateTime;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.Date;
import java.util.Collection;

@Mapper(componentModel = "spring")
public interface RuleGroupRoleMapper {
    @Named("mapBasicRuleGroupRoleDTOToRuleGroupRoleForSave")
    @Mapping(target = "ruleGroupRoleId", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "updatedAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "roleId.roleId", source = "roleId")
    @Mapping(target = "ruleGroupId.ruleGroupId", source = "ruleGroupId")
    RuleGroupRole mapBasicRuleGroupRoleDTOToRuleGroupRoleForSave(RuleGroupRoleDTO ruleGroupRoleDTO);
    @Named("mapBasicRuleGroupRoleDTOCollectionToRuleGroupRoleCollectionForRuleGroupSave")
    @IterableMapping(qualifiedByName = "mapBasicRuleGroupRoleDTOToRuleGroupRoleForSave")
    Collection<RuleGroupRole> mapBasicRuleGroupRoleDTOCollectionToRuleGroupRoleCollectionForRuleGroupSave(Collection<RuleGroupRoleDTO> ruleGroupRoleDTOs);
    @Named("mapBasicRuleGroupRoleDTOToRuleGroupRoleForUpdate")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "updatedAt",  expression = "java(getCurrentTimestamp())")
    @Mapping(target = "roleId.roleId", source = "roleId")
    @Mapping(target = "ruleGroupId.ruleGroupId", source = "ruleGroupId")
    RuleGroupRole mapBasicRuleGroupRoleDTOToRuleGroupRoleForUpdate(RuleGroupRoleDTO ruleGroupRoleDTO);
    @Named("mapBasicRuleGroupRoleDTOCollectionToRuleGroupRoleCollectionForRuleGroupUpdate")
    @IterableMapping(qualifiedByName = "mapBasicRuleGroupRoleDTOToRuleGroupRoleForUpdate")
    Collection<RuleGroupRole> mapBasicRuleGroupRoleDTOCollectionToRuleGroupRoleCollectionForRuleGroupUpdate(Collection<RuleGroupRoleDTO> ruleGroupRoleDTOs);
    @Named("mapBasicRuleGroupRoleToRuleGroupRoleDTOForGet")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "roleId", source = "roleId.roleId")
    @Mapping(target = "ruleGroupId", source = "ruleGroupId.ruleGroupId")
    RuleGroupRoleDTO mapBasicRuleGroupRoleToRuleGroupRoleDTOForGet(RuleGroupRole ruleGroupRole);
    @Named("mapBasicRuleGroupRoleCollectionToRuleGroupRoleDTOCollectionForRuleGroupGet")
    @IterableMapping(qualifiedByName = "mapBasicRuleGroupRoleToRuleGroupRoleDTOForGet")
    Collection<RuleGroupRoleDTO> mapBasicRuleGroupRoleCollectionToRuleGroupRoleDTOCollectionForRuleGroupGet(Collection<RuleGroupRole> ruleGroupRoles);
    default Date getCurrentTimestamp() {
        return DateTime.getCurrentTimestamp();
    }
}
