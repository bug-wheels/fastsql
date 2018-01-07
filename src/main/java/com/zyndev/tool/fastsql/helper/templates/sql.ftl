
<#list tableInfos as tableInfo>

-- ----------------------------
-- Table structure for ${tableInfo.tableName}
-- ${tableInfo.tableComment}
-- ----------------------------
DROP TABLE IF EXISTS `${tableInfo.tableName}`;
CREATE TABLE `${tableInfo.tableName}` (
<#list tableInfo.columnInfoList as field>
    `${field.column}` ${field.type} COMMENT '${field.comment}',
</#list>
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='${tableInfo.tableComment}';
</#list>