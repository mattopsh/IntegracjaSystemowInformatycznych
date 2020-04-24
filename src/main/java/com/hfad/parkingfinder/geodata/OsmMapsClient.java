package com.hfad.parkingfinder.geodata.osm;

import com.hfad.parkingfinder.geodata.MapClient;
import com.hfad.parkingfinder.geodata.osm.model.ProviderOsmType;
import com.hfad.parkingfinder.geodata.osm.model.dto.AddressOsmDto;
import com.hfad.parkingfinder.geodata.osm.model.dto.ParkingOsmDto;
import com.hfad.parkingfinder.geodata.osm.model.dto.ProviderOsmDto;
import com.hfad.parkingfinder.geodata.osm.model.dto.ProvidersOsmDto;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OsmMapsClient implements MapClient {

    private WebClient nominatimClient;
    private WebClient overpassClient;

    @Autowired
    public OsmMapsClient(Environment env) {
        overpassClient = WebClient.builder().baseUrl(Objects.requireNonNull(env.getProperty("osm.api.overpass"))).build();
        nominatimClient = WebClient.builder().baseUrl(Objects.requireNonNull(env.getProperty("osm.api.nominatim"))).build();
    }

    @Override
    public Mono<Long> findNearestParkingId(double attitude, double longitude) {
        return nominatimClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .queryParam("q", "parking+near+" + attitude + "+" + longitude)
                                .queryParam("format", "json")
                                .queryParam("extratags", 1)
                                .queryParam("addressdetails", 1)
                                .queryParam("limit", 1)
                                .build())
                .retrieve()
                .bodyToFlux(ParkingOsmDto.class)
                .next()
                .map(ParkingOsmDto::getParkingNodeId);
    }

    @Override
    public Flux<ProviderOsmDto> findProviders(List<ProviderOsmType> providerOsmTypes, int maxDistance, String providerName, double attitude, double longitude) {
        if (providerOsmTypes.isEmpty()) {
            return findAllTypesOfProviders(maxDistance, providerName, attitude, longitude);
        } else {
            val responseType = "?data=[out:json];";
            val around = "(around:" + maxDistance + "," + attitude + "," + longitude + ");";
            val providerTypeQuery = enumToStringQuery(providerOsmTypes);
            val query = responseType + "(node[\"shop\"~\"" + providerTypeQuery + "\"]" + around + ");out;>;";
            return overpassClient.get()
                    .uri(query)
                    .retrieve()
                    .bodyToMono(ProvidersOsmDto.class)
                    .flatMapIterable(ProvidersOsmDto::getProviderOsmDto)
                    .filter(providerOsmDto -> filterProviderName(providerOsmDto, providerName));
        }
    }

    private Flux<ProviderOsmDto> findAllTypesOfProviders(int maxDistance, String providerName, double attitude, double longitude) {
        val responseType = "?data=[out:json];";
        val around = "(around:" + maxDistance + "," + attitude + "," + longitude + ");";
        val query = responseType + "(node[\"shop\"]" + around + ");out;>;";
        return overpassClient.get()
                .uri(query)
                .retrieve()
                .bodyToMono(ProvidersOsmDto.class)
                .flatMapIterable(ProvidersOsmDto::getProviderOsmDto)
                .filter(providerOsmDto -> filterProviderName(providerOsmDto, providerName));
    }

    private boolean filterProviderName(ProviderOsmDto providerOsmDto, String providerName) {
        if (providerName.isEmpty()) {
            return true;
        } else {
            val hasName = providerOsmDto.getProviderOsmDetailsDto() != null && providerOsmDto.getProviderOsmDetailsDto().getName() != null;
            if (hasName) {
                return providerOsmDto.getProviderOsmDetailsDto().getName().toLowerCase().contains(providerName.toLowerCase());
            } else {
                return false;
            }

        }
    }

    private String enumToStringQuery(List<? extends Enum> enums) {
        return String.join("|", enums.stream().map(e -> e.name().toLowerCase()).collect(Collectors.toList()));
    }

}
