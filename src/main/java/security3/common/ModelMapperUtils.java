package security3.common;

import org.modelmapper.ModelMapper;

public class ModelMapperUtils {
    private static ModelMapper modelMapper = new ModelMapper();

    public static <D, T> D map(final T entity, final Class<D> outClass) {
        if(entity == null) {
            return null;
        }
        return modelMapper.map(entity, outClass);
    }
}
