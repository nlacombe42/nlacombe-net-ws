package net.nlacombe.nlacombenetws.shared;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class BeanMapper<DtoType, EntityType> implements Mapper<DtoType, EntityType>
{
	private Class<DtoType> dtoClass;
	private Class<EntityType> entityClass;

	public BeanMapper(Class<DtoType> dtoClass, Class<EntityType> entityClass)
	{
		this.dtoClass = dtoClass;
		this.entityClass = entityClass;
	}

	@Override
	public EntityType mapToEntity(DtoType dto)
	{
		if (dto == null)
			return null;


        try {
            EntityType entity = entityClass.getDeclaredConstructor().newInstance();

            BeanUtils.copyProperties(dto, entity);

            return entity;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Error instantiating class.", e);
        }
	}

	@Override
	public DtoType mapToDto(EntityType entity)
	{
		if (entity == null)
			return null;

        try {
            DtoType dto = dtoClass.getDeclaredConstructor().newInstance();

            BeanUtils.copyProperties(entity, dto);

            return dto;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Error instantiating class.", e);
        }
    }
}
