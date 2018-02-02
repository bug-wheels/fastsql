package ${packageName};

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * ${tableColumn}
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 0.0.1
 * @date ${createDate?string("yyyy-MM-dd HH:mm:ss")}
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "${tableName}")
public class ${className} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list fields as field>
    /**
     * ${field.comment}
     */
    <#if field.id>
    @Id
    </#if>
    <#if field.name == field.column>
    @Column
    <#else>
    @Column(name = "${field.column}")
    </#if>
    private ${field.type} ${field.name};

</#list>
}
