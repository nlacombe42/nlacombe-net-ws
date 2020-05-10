package net.nlacombe.nlacombenetws.shared;

public interface Mapper<DtoType, EntityType>
{
	EntityType mapToEntity(DtoType dto);

	DtoType mapToDto(EntityType entity);
}
