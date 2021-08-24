package com.tacs.grupo1.covid.api.bootstrap;

import com.tacs.grupo1.covid.api.domain.*;
import com.tacs.grupo1.covid.api.exceptions.NotFoundException;
import com.tacs.grupo1.covid.api.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class BootstrapDataPopulator {

    private final CountryService countryService;
    private final CountryListService countryListService;
    private final UserService userService;
    private final RoleService roleService;
    private final SessionService sessionService;
    private final PrivilegeService privilegeService;
    private final StatisticsService statisticsService;
    private final PlotService plotService;
    @Value("${mockcountrycreation}")
    private String mockCountryCreation = "false";


    @Autowired
    public BootstrapDataPopulator(CountryService countryService, CountryListService countryListService, UserService userService, RoleService roleService, SessionService sessionService, PrivilegeService privilegeService, StatisticsService statisticsService, PlotService plotService) {
        this.countryService = countryService;
        this.countryListService = countryListService;
        this.userService = userService;
        this.roleService = roleService;
        this.sessionService = sessionService;
        this.privilegeService = privilegeService;
        this.statisticsService = statisticsService;
        this.plotService = plotService;
    }

    @PostConstruct
    private void init(){
        //check if there's data
        try {
            userService.get(1L);
        }catch (NotFoundException e)
        {
            createInitialData();
        }
        sessionService.getRandomEncoder();
    }

    private void createInitialData() {
        //PRIVILEGE GENERATION
        PrivilegeEntity adminPrivilege = new PrivilegeEntity().setName("ADMIN");
        privilegeService.save(adminPrivilege);
        PrivilegeEntity userPrivilege = new PrivilegeEntity().setName("USER");
        privilegeService.save(userPrivilege);
        PrivilegeEntity telegramPrivilege = new PrivilegeEntity().setName("TELEGRAM");
        privilegeService.save(telegramPrivilege);

        List<PrivilegeEntity> adminPrivileges = Arrays.asList(adminPrivilege, userPrivilege);

        //ROLES GENERATION
        RoleEntity role_admin = new RoleEntity().setName("ROLE_ADMIN").setPrivileges(adminPrivileges);
        RoleEntity role_user = new RoleEntity().setName("ROLE_USER").setPrivileges(Arrays.asList(userPrivilege));
        RoleEntity role_telegram = new RoleEntity().setName("ROLE_TELEGRAM").setPrivileges(Arrays.asList(telegramPrivilege));
        roleService.save(role_admin);
        roleService.save(role_user);
        roleService.save(role_telegram);

        //USER DATA GENERATION
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        UserEntity user3 = new UserEntity();

        user1.setUserName("Administrator")
                .setName("Juan")
                .setLastName("Perez")
                .setEmail("juanperez@aol.com")
                .setPassword(sessionService.hashPassword("123456"))
                .setRoles(Arrays.asList(role_admin));


        user2.setUserName("Usuario1")
                .setName("Guillermo")
                .setLastName("Francella")
                .setEmail("guillermofrancella@sion.com")
                .setPassword(sessionService.hashPassword("654321"))
                .setRoles(Arrays.asList(role_user));

        user3.setUserName("TelegramBot")
                .setName("TelegramBot")
                .setLastName("TelegramBot")
                .setEmail("TelegramBot")
                .setPassword(sessionService.hashPassword("12345678"))
                .setRoles(Arrays.asList(role_telegram));

        user1 = userService.save(user1);
        user2 = userService.save(user2);
        user3 = userService.save(user3);

        //HORRIBLE
        role_admin.setUsers(Arrays.asList(user1));
        role_user.setUsers(Arrays.asList(user2));
        role_telegram.setUsers(Arrays.asList(user3));
        adminPrivilege.setRoles(Arrays.asList(role_admin));
        userPrivilege.setRoles(Arrays.asList(role_user));
        telegramPrivilege.setRoles(Arrays.asList(role_telegram));
        userService.update(user1.getId(), user1);
        userService.update(user2.getId(), user2);
        userService.update(user3.getId(), user3);

//  ******************************************************************************************
        // false: carga todos los paises desde la api externa
        // true: carga los 10 paises de forma local
        if (mockCountryCreation.equalsIgnoreCase("false")){

            //GET ALL COUNTRY INFORMATION FROM A 3rd PARTY API
            log.info("Cargando países, por favor espere...");
            countryService.initializeDB();

            //GET ALL STATISTICS FROM THE COUNTRIES
            log.info("Calculando estadísticas de los países, por favor espere...");
            statisticsService.initializeDB();

            //CLEAN EMPTY COUNTRIES
            log.info("Eliminando países sin info...");
            countryService.purgeDB();

            CountryListEntity countryList1 = new CountryListEntity().setName("Lista 11").setUser(user1).setCountries(new HashSet<>());
            try {
                countryList1.setCreationDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/06/2020"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            countryList1.getCountries().add(countryService.getByIsoCountryCode("AR"));
            countryList1.getCountries().add(countryService.getByIsoCountryCode("BR"));
            countryList1.getCountries().add(countryService.getByIsoCountryCode("CL"));
            countryListService.save(countryList1);

            CountryListEntity countryList2 = new CountryListEntity().setName("Lista 12").setUser(user1).setCountries(new HashSet<>());
            try {
                countryList2.setCreationDate(new SimpleDateFormat("dd/MM/yyyy").parse("06/06/2020"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            countryList2.getCountries().add(countryService.getByIsoCountryCode("UY"));
            countryList2.getCountries().add(countryService.getByIsoCountryCode("PY"));
            countryList2.getCountries().add(countryService.getByIsoCountryCode("PE"));
            countryListService.save(countryList2);

            CountryListEntity countryList3 = new CountryListEntity().setName("Lista 21").setUser(user2).setCountries(new HashSet<>());
            try {
                countryList3.setCreationDate(new SimpleDateFormat("dd/MM/yyyy").parse("06/06/2020"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            countryList3.getCountries().add(countryService.getByIsoCountryCode("BO"));
            countryListService.save(countryList3);

        }
        else{
            //COUNTRY DATA GENERATION
            CountryEntity country1 = new CountryEntity().setName("Argentina").setIsoCountryCode("AR")
                    .setStartDate(statisticsService.getCountryStartDay("AR"))
                    .setOffsetVal(statisticsService.getCountryOffset("AR"))
                    .setStrategy("Cuarentena");
            countryService.save(country1);
            CountryEntity country2 = new CountryEntity().setName("Brasil").setIsoCountryCode("BR")
                    .setStartDate(statisticsService.getCountryStartDay("BR"))
                    .setOffsetVal(statisticsService.getCountryOffset("BR"))
                    .setStrategy("Libre Circulación");
            countryService.save(country2);
            CountryEntity country3 = new CountryEntity().setName("USA").setIsoCountryCode("US")
                    .setStartDate(statisticsService.getCountryStartDay("US"))
                    .setOffsetVal(statisticsService.getCountryOffset("US"));
            countryService.save(country3);
            CountryEntity country4 = new CountryEntity().setName("China").setIsoCountryCode("CN")
                    .setStartDate(statisticsService.getCountryStartDay("CN"))
                    .setOffsetVal(statisticsService.getCountryOffset("CN"));
            countryService.save(country4);
            CountryEntity country5 = new CountryEntity().setName("Italia").setIsoCountryCode("IT")
                    .setStartDate(statisticsService.getCountryStartDay("IT"))
                    .setOffsetVal(statisticsService.getCountryOffset("IT"));
            countryService.save(country5);
            CountryEntity country6 = new CountryEntity().setName("Chile").setIsoCountryCode("CL")
                    .setStartDate(statisticsService.getCountryStartDay("CL"))
                    .setOffsetVal(statisticsService.getCountryOffset("CL"));
            countryService.save(country6);
            CountryEntity country7 = new CountryEntity().setName("Uruguay").setIsoCountryCode("UY")
                    .setStartDate(statisticsService.getCountryStartDay("UY"))
                    .setOffsetVal(statisticsService.getCountryOffset("UY"))
                    .setStrategy("Distanciamiento Social");
            countryService.save(country7);
            CountryEntity country8 = new CountryEntity().setName("Paraguay").setIsoCountryCode("PY")
                    .setStartDate(statisticsService.getCountryStartDay("PY"))
                    .setOffsetVal(statisticsService.getCountryOffset("PY"));
            countryService.save(country8);
            CountryEntity country9 = new CountryEntity().setName("Bolivia").setIsoCountryCode("BO")
                    .setStartDate(statisticsService.getCountryStartDay("BO"))
                    .setOffsetVal(statisticsService.getCountryOffset("BO"));
            countryService.save(country9);
            CountryEntity country10 = new CountryEntity().setName("Reino Unido").setIsoCountryCode("GB")
                    .setStartDate(statisticsService.getCountryStartDay("GB"))
                    .setOffsetVal(statisticsService.getCountryOffset("GB"));
            countryService.save(country10);

            System.out.println(country1.getIsoCountryCode() + " - " + country1.getOffsetVal() + " - " + country1.getStartDate());
            System.out.println(country2.getIsoCountryCode() + " - " + country2.getOffsetVal() + " - " + country2.getStartDate());
            System.out.println(country3.getIsoCountryCode() + " - " + country3.getOffsetVal() + " - " + country3.getStartDate());
            System.out.println(country4.getIsoCountryCode() + " - " + country4.getOffsetVal() + " - " + country4.getStartDate());
            System.out.println(country5.getIsoCountryCode() + " - " + country5.getOffsetVal() + " - " + country5.getStartDate());
            System.out.println(country6.getIsoCountryCode() + " - " + country6.getOffsetVal() + " - " + country6.getStartDate());
            System.out.println(country7.getIsoCountryCode() + " - " + country7.getOffsetVal() + " - " + country7.getStartDate());
            System.out.println(country8.getIsoCountryCode() + " - " + country8.getOffsetVal() + " - " + country8.getStartDate());
            System.out.println(country9.getIsoCountryCode() + " - " + country9.getOffsetVal() + " - " + country9.getStartDate());
            System.out.println(country10.getIsoCountryCode() + " - " + country10.getOffsetVal() + " - " + country10.getStartDate());

            CountryListEntity countryList1 = new CountryListEntity().setName("Lista 11").setUser(user1).setCountries(new HashSet<>());
            try {
                countryList1.setCreationDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/06/2020"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            countryList1.getCountries().add(country1);
            countryList1.getCountries().add(country2);
            countryList1.getCountries().add(country3);
            countryListService.save(countryList1);

            CountryListEntity countryList2 = new CountryListEntity().setName("Lista 12").setUser(user1).setCountries(new HashSet<>());
            try {
                countryList2.setCreationDate(new SimpleDateFormat("dd/MM/yyyy").parse("06/06/2020"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            countryList2.getCountries().add(country1);
            countryList2.getCountries().add(country4);
            countryList2.getCountries().add(country5);
            countryListService.save(countryList2);

            CountryListEntity countryList3 = new CountryListEntity().setName("Lista 21").setUser(user2).setCountries(new HashSet<>());
            try {
                countryList3.setCreationDate(new SimpleDateFormat("dd/MM/yyyy").parse("06/06/2020"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            countryList3.getCountries().add(country1);
            countryListService.save(countryList3);
        }

    }
}
