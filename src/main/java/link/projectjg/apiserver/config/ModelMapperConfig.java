package link.projectjg.apiserver.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    ModelMapper modelMapper = new ModelMapper();

    @Bean
    public ModelMapper modelMapper() {
        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }
}
