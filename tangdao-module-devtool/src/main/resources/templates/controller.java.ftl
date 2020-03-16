package ${package.Controller};


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * @Todo ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping(value = "/{env}/${table.entityPath}s", produces = MediaType.APPLICATION_JSON_VALUE)
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

	@Autowired
	private ${table.serviceName} ${table.entityPath}Service;
	
	/**
	 * @Todo ${table.comment!}分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping
	public IPage<${entity}> page(Page<${entity}> page) {
		return ${table.entityPath}Service.page(page);
	}

	/**
	 * @Todo ${table.comment!}信息
	 * 
	 * @param ${table.entityPath}Id
	 * @return
	 */
	@GetMapping("/{${table.entityPath}Id}")
	public ${entity} get(String ${table.entityPath}Id) {
		return ${table.entityPath}Service.getById(${table.entityPath}Id);
	}

	/**
	 * @Todo ${table.comment!}保存
	 * 
	 * @param ${table.entityPath}
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody @Validated ${entity} ${table.entityPath}) {
		return ${table.entityPath}Service.save(${table.entityPath});
	}

	/**
	 * @Todo ${table.comment!}修改
	 * 
	 * @param ${table.entityPath}Id
	 * @param ${table.entityPath}
	 * @return
	 */
	@PutMapping("/{${table.entityPath}Id}")
	public boolean update(@PathVariable("${table.entityPath}Id") String ${table.entityPath}Id, @RequestBody @Validated ${entity} ${table.entityPath}) {
		return ${table.entityPath}Service.saveOrUpdate(${table.entityPath});
	}

	/**
	 * @Todo ${table.comment!}删除
	 * 
	 * @param ${table.entityPath}Id
	 * @return
	 */
	@DeleteMapping("/{${table.entityPath}Id}")
	public boolean delete(@PathVariable("${table.entityPath}Id") String ${table.entityPath}Id) {
		return ${table.entityPath}Service.removeById(${table.entityPath}Id);
	}
}
</#if>
