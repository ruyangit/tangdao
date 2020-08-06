package ${package.Service};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import org.springframework.stereotype.Service;
import com.tangdao.core.service.BaseService;

/**
 * <p>
 * TODO ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${entity}Service extends BaseService<${table.mapperName}, ${entity}> {

}
