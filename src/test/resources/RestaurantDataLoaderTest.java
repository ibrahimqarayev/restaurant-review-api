package com.ibrahim.restaurant.manual;

import com.ibrahim.restaurant.api.restaurant.RestaurantCreateRequest;
import com.ibrahim.restaurant.entity.Address;
import com.ibrahim.restaurant.entity.OperatingHours;
import com.ibrahim.restaurant.entity.Photo;
import com.ibrahim.restaurant.entity.TimeRange;
import com.ibrahim.restaurant.service.PhotoService;
import com.ibrahim.restaurant.service.RestaurantService;
import com.ibrahim.restaurant.service.impl.RandomBakuGeoLocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class RestaurantDataLoaderTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RandomBakuGeoLocationService geoLocationService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    @Rollback(false)
    public void createSampleRestaurants() throws Exception {
        List<RestaurantCreateRequest> restaurants = createRestaurantData();
        restaurants.forEach(restaurant -> {
            String fileName = restaurant.getPhotoIds().get(0);
            Resource resource = resourceLoader.getResource("classpath:testdata/" + fileName);
            MultipartFile multipartFile = null;
            try {
                multipartFile = new MockMultipartFile(
                        "file",
                        fileName,
                        MediaType.IMAGE_PNG_VALUE,
                        resource.getInputStream()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Photo uploadedPhoto = photoService.uploadPhoto(multipartFile);
            restaurant.setPhotoIds(List.of(uploadedPhoto.getUrl()));
            restaurantService.createRestaurant(restaurant);
            System.out.println("Created restaurant: " + restaurant.getName());
        });
    }

    private List<RestaurantCreateRequest> createRestaurantData() {
        return Arrays.asList(
                createRestaurant(
                        "Baku BBQ House",
                        "Azerbaijani",
                        "+994 12 345 6789",
                        createAddress("10", "Fountain Square", null, "Baku", "Baku", "AZ1000", "Azerbaijan"),
                        createStandardOperatingHours("11:00", "23:00", "11:00", "23:30"),
                        "baku-bbq-house.png"
                ),
                createRestaurant(
                        "Caspian Fish Grill",
                        "Seafood",
                        "+994 12 345 6790",
                        createAddress("5", "Nizami Street", null, "Baku", "Baku", "AZ1001", "Azerbaijan"),
                        createStandardOperatingHours("12:00", "22:30", "12:00", "23:00"),
                        "caspian-fish-grill.png"
                ),
                createRestaurant(
                        "Flame Towers Cafe",
                        "Cafe",
                        "+994 12 345 6791",
                        createAddress("20", "Upland Avenue", null, "Baku", "Baku", "AZ1002", "Azerbaijan"),
                        createStandardOperatingHours("10:00", "22:00", "10:00", "22:30"),
                        "flame-towers-cafe.png"
                ),
                createRestaurant(
                        "Old City Bistro",
                        "International",
                        "+994 12 345 6792",
                        createAddress("15", "Icherisheher Street", null, "Baku", "Baku", "AZ1003", "Azerbaijan"),
                        createStandardOperatingHours("11:30", "23:00", "11:30", "23:30"),
                        "old-city-bistro.png"
                ),
                createRestaurant(
                        "Nizami Lounge",
                        "Cafe",
                        "+994 12 345 6793",
                        createAddress("7", "Nizami Street", null, "Baku", "Baku", "AZ1004", "Azerbaijan"),
                        createStandardOperatingHours("10:00", "22:30", "10:00", "23:00"),
                        "nizami-lounge.png"
                )
        );
    }

    private RestaurantCreateRequest createRestaurant(
            String name,
            String cuisineType,
            String contactInformation,
            Address address,
            OperatingHours operatingHours,
            String photoId
    ) {
        return RestaurantCreateRequest.builder()
                .name(name)
                .cuisineType(cuisineType)
                .contactInformation(contactInformation)
                .address(address)
                .operatingHours(operatingHours)
                .photoIds(List.of(photoId))
                .build();
    }

    private Address createAddress(
            String streetNumber,
            String streetName,
            String unit,
            String city,
            String state,
            String postalCode,
            String country
    ) {
        Address address = new Address();
        address.setStreetNumber(streetNumber);
        address.setStreetName(streetName);
        address.setUnit(unit);
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(postalCode);
        address.setCountry(country);
        return address;
    }

    private OperatingHours createStandardOperatingHours(
            String weekdayOpen,
            String weekdayClose,
            String weekendOpen,
            String weekendClose
    ) {
        TimeRange weekday = new TimeRange();
        weekday.setOpenTime(weekdayOpen);
        weekday.setCloseTime(weekdayClose);

        TimeRange weekend = new TimeRange();
        weekend.setOpenTime(weekendOpen);
        weekend.setCloseTime(weekendClose);

        OperatingHours hours = new OperatingHours();
        hours.setMonday(weekday);
        hours.setTuesday(weekday);
        hours.setWednesday(weekday);
        hours.setThursday(weekday);
        hours.setFriday(weekend);
        hours.setSaturday(weekend);
        hours.setSunday(weekend);

        return hours;
    }
}