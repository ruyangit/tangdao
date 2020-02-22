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
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
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
@RequestMapping(value = "/api/{env}<#if package.ModuleName??>/${package.ModuleName}</#if>", produces = MediaType.APPLICATION_JSON_VALUE)
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
	 * ${table.comment!}分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/${table.entityPath}")
	public IPage<${entity}> page(IPage<${entity}> page) {
		return ${table.entityPath}Service.page(page);
	}

	/**
	 * ${table.comment!}列表
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/${table.entityPath}s")
	public List<${entity}> list() {
		return ${table.entityPath}Service.list();
	}

	/**
	 * ${table.comment!}信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/${table.entityPath}/{id}")
	public ${entity} get${entity}(String id) {
		return ${table.entityPath}Service.getById(id);
	}

	/**
	 * ${table.comment!}保存
	 * 
	 * @param ${table.entityPath}
	 * @return
	 */
	@PostMapping("/${table.entityPath}")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody @Validated ${entity} ${table.entityPath}) {
		return ${table.entityPath}Service.save(${table.entityPath});
	}

	/**
	 * ${table.comment!}修改
	 * 
	 * @param id
	 * @param ${table.entityPath}
	 * @return
	 */
	@PutMapping("/${table.entityPath}")
	public boolean update(@RequestBody @Validated ${entity} ${table.entityPath}) {
		return ${table.entityPath}Service.saveOrUpdate(${table.entityPath});
	}

	/**
	 * ${table.comment!}删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/${table.entityPath}/{id}")
	public boolean delete(@PathVariable("id") String id) {
		return ${table.entityPath}Service.removeById(id);
	}
}
</#if>
