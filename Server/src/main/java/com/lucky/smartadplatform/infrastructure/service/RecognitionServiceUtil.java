package com.lucky.smartadplatform.infrastructure.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.lucky.smartadplatform.domain.Category;
import com.lucky.smartadplatform.domain.service.CategoryService;
import com.lucky.smartadplatform.infrastructure.properties.SentisightProperties;
import com.lucky.smartadplatform.infrastructure.util.FileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecognitionServiceUtil {

    private List<String> recognitionClasses;

    private Map<String, List<String>> categoryMap;

    @Autowired
    private SentisightProperties sentisightProperties;

    @Autowired
    private CategoryService categoryService;

    @PostConstruct
    public void loadRecognitionClasses() throws IOException {
        this.recognitionClasses = new ArrayList<>();
        String recognitionClassesFile = FileUtil.getFilePath(sentisightProperties.getRecognitionClassFilePath());
        File file = new File(recognitionClassesFile);

        try (FileReader fileReader = new FileReader(file)) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    this.recognitionClasses.add(line);
                }
            }
        } catch (Exception e) {
            log.error("Failed to read the recognition classes file!");
            e.printStackTrace();
        }
        mapCategoriesToClasses();
        createDefaultRootCategories();
    }

    public List<String> getRecognitionClasses() {
        return this.recognitionClasses;
    }

    public String getCategoryNameFromPredicted(String predictedString) {
        for (var mapEntry : this.categoryMap.entrySet()) {
            for (String predictionClass : mapEntry.getValue()) {
                if (predictionClass.equals(predictedString)) {
                    return mapEntry.getKey();
                }
            }
        }
        return null;
    }

    private void createDefaultRootCategories() {
        Category rootCategory = null;

        Category clothing = null;
        Category sunglasses = null;
        Category hats = null;
        Category clothingTops = null;
        Category clothingBottoms = null;
        Category shoes = null;

        Category arts = null;
        Category stringMusicInstruments = null;

        Category electronics = null;
        Category computers = null;
        Category mobileElectronics = null;

        Category householdItems = null;
        Category officeItems = null;
        Category furniture = null;

        Category outdoors = null;

        Category sports = null;

        Category automotive = null;
        Category cars = null;
        Category motorbikes = null;
        Category automotiveParts = null;
        try {
            rootCategory = categoryService.createCategory("All", null);
        } catch (Exception e) {
            log.info(e.getMessage());
            return;
        }

        try {
            clothing = categoryService.createCategory("Clothing", rootCategory.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            sunglasses = categoryService.createCategory("Sunglasses", clothing.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            hats = categoryService.createCategory("Hats", clothing.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            clothingTops = categoryService.createCategory("Clothing tops", clothing.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            clothingBottoms = categoryService.createCategory("Clothing bottoms", clothing.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            shoes = categoryService.createCategory("Shoes", clothing.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        try {
            arts = categoryService.createCategory("Arts", rootCategory.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            stringMusicInstruments = categoryService.createCategory("String music instruments", arts.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        try {
            electronics = categoryService.createCategory("Electronics", rootCategory.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            computers = categoryService.createCategory("Computers", electronics.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            mobileElectronics = categoryService.createCategory("Mobile electronics", electronics.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        try {
            householdItems = categoryService.createCategory("Household items", rootCategory.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            officeItems = categoryService.createCategory("Office items", householdItems.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            furniture = categoryService.createCategory("Furniture", householdItems.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        try {
            outdoors = categoryService.createCategory("Outdoors", rootCategory.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        try {
            sports = categoryService.createCategory("Sports", rootCategory.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        try {
            automotive = categoryService.createCategory("Automotive", rootCategory.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            cars = categoryService.createCategory("Cars", automotive.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            motorbikes = categoryService.createCategory("Motorbikes", automotive.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
        try {
            automotiveParts = categoryService.createCategory("Automotive parts", automotive.getId());
        } catch (IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    private void mapCategoriesToClasses() {
        this.categoryMap = new HashMap<>();
        mapClothing();
        mapArts();
        mapElectronics();
        mapHouseholdItems();
        mapOutdoors();
        mapSports();
        mapAutomotive();
    }

    private void mapClothing() {
        List<String> classes = new ArrayList<>();
        classes.add("binder, ring-binder");
        classes.add("bolo tie, bolo, bola tie, bola");
        classes.add("bow tie, bow-tie, bowtie");
        classes.add("digital watch");
        classes.add("knot");
        classes.add("ladle");
        classes.add("mailbag, postbag");
        classes.add("military uniform");
        classes.add("mitten");
        classes.add("necklace");
        classes.add("purse");
        classes.add("sock");
        classes.add("suit, suit of clothes");
        classes.add("velvet");
        classes.add("wool, woolen, woollen");
        classes.add("book jacket, dust cover, dust jacket, dust wrapper");

        this.categoryMap.put("Clothing", classes);
        mapClothingHats();
        mapClothingSunglasses();
        mapClothingTops();
        mapClothingBottoms();
        mapClothingShoes();
    }

    private void mapClothingHats() {
        List<String> classes = new ArrayList<>();
        classes.add("cowboy hat, ten-gallon hat");
        classes.add("crash helmet");
        classes.add("gasmask, respirator, gas helmet");
        classes.add("mask");
        classes.add("neck brace");
        classes.add("shower cap");
        classes.add("ski mask");
        classes.add("sombrero");
        classes.add("wig");

        this.categoryMap.put("Hats", classes);
    }

    private void mapClothingSunglasses() {
        List<String> classes = new ArrayList<>();
        classes.add("sunglass");
        classes.add("sunglasses, dark glasses, shades");

        this.categoryMap.put("Sunglasses", classes);
    }

    private void mapClothingTops() {
        List<String> classes = new ArrayList<>();
        classes.add("bikini, two-piece");
        classes.add("breastplate, aegis, egis");
        classes.add("chest");
        classes.add("cloak");
        classes.add("cuirass");
        classes.add("fur coat");
        classes.add("jersey, T-shirt, tee shirt");
        classes.add("kimono");
        classes.add("lab coat, laboratory coat");
        classes.add("maillot");
        classes.add("maillot, tank suit");
        classes.add("sweatshirt");
        classes.add("trench coat");

        this.categoryMap.put("Clothing tops", classes);
    }

    private void mapClothingBottoms() {
        List<String> classes = new ArrayList<>();
        classes.add("jean, blue jean, denim");
        classes.add("knee pad");
        classes.add("miniskirt, mini");
        classes.add("overskirt");
        classes.add("swimming trunks, bathing trunks");

        this.categoryMap.put("Clothing bottoms", classes);
    }

    private void mapClothingShoes() {
        List<String> classes = new ArrayList<>();
        classes.add("cowboy boot");
        classes.add("running shoe");
        classes.add("sandal");

        this.categoryMap.put("Shoes", classes);
    }

    private void mapArts() {
        List<String> classes = new ArrayList<>();
        classes.add("accordion, piano accordion, squeeze box");
        classes.add("cornet, horn, trumpet, trump");
        classes.add("drum, membranophone, tympan");
        classes.add("drumstick");
        classes.add("flute, transverse flute");
        classes.add("grand piano, grand");
        classes.add("harmonica, mouth organ, harp, mouth harp");
        classes.add("harvester, reaper");
        classes.add("marimba, xylophone");
        classes.add("organ, pipe organ");
        classes.add("pick, plectrum, plectron");
        classes.add("sax, saxophone");
        classes.add("trombone");
        classes.add("upright, upright piano");

        this.categoryMap.put("Arts", classes);
        mapArtsStringInstruments();
    }

    private void mapArtsStringInstruments() {
        List<String> classes = new ArrayList<>();
        classes.add("acoustic guitar");
        classes.add("banjo");
        classes.add("cello, violoncello");
        classes.add("electric guitar");
        classes.add("harp");
        classes.add("violin, fiddle");

        this.categoryMap.put("String music instruments", classes);
    }

    private void mapElectronics() {
        List<String> classes = new ArrayList<>();
        classes.add("analog clock");
        classes.add("cassette");
        classes.add("cassette player");
        classes.add("CD player");
        classes.add("digital clock");
        classes.add("entertainment center");
        classes.add("espresso maker");
        classes.add("iron, smoothing iron");
        classes.add("loudspeaker, speaker, speaker unit, loudspeaker system, speaker system");
        classes.add("magnetic compass");
        classes.add("microphone, mike");
        classes.add("microwave, microwave oven");
        classes.add("modem");
        classes.add("projector");
        classes.add("radio, wireless");
        classes.add("radio telescope, radio reflector");
        classes.add("reflex camera");
        classes.add("refrigerator, icebox");
        classes.add("remote control, remote");
        classes.add("scale, weighing machine");
        classes.add("sewing machine");
        classes.add("stopwatch, stop watch");
        classes.add("stove");
        classes.add("switch, electric switch, electrical switch");
        classes.add("television, television system");
        classes.add("toaster");
        classes.add("typewriter keyboard");
        classes.add("vacuum, vacuum cleaner");
        classes.add("waffle iron");
        classes.add("wall clock");

        this.categoryMap.put("Electronics", classes);
        mapElectronicsComputers();
        mapElectronicsMobile();
    }

    private void mapElectronicsComputers() {
        List<String> classes = new ArrayList<>();
        classes.add("computer keyboard, keypad");
        classes.add("desktop computer");
        classes.add("hand-held computer, hand-held microcomputer");
        classes.add("joystick");
        classes.add("laptop, laptop computer");
        classes.add("monitor");
        classes.add("mouse, computer mouse");
        classes.add("printer");
        classes.add("screen, CRT screen");
        classes.add("notebook, notebook computer");
        classes.add("web site, website, internet site, site");

        this.categoryMap.put("Computers", classes);
    }

    private void mapElectronicsMobile() {
        List<String> classes = new ArrayList<>();
        classes.add("cellular telephone, cellular phone, cellphone, cell, mobile phone");
        classes.add("dial telephone, dial phone");
        classes.add("iPod");

        this.categoryMap.put("Mobile electronics", classes);
    }

    private void mapHouseholdItems() {
        List<String> classes = new ArrayList<>();
        classes.add(
                "ashcan, trash can, garbage can, wastebin, ash bin, ash-bin, ashbin, dustbin, trash barrel, trash bin");
        classes.add("balloon");
        classes.add("bath towel");
        classes.add("beer glass");
        classes.add("beer bottle");
        classes.add("bottlecap");
        classes.add("broom");
        classes.add("bucket, pail");
        classes.add("buckle");
        classes.add("bulletproof vest");
        classes.add("caldron, cauldron");
        classes.add("candle, taper, wax light");
        classes.add("can opener, tin opener");
        classes.add("chime, bell, gong");
        classes.add("Christmas stocking");
        classes.add("cleaver, meat cleaver, chopper");
        classes.add("cocktail shaker");
        classes.add("coffee mug");
        classes.add("coffeepot");
        classes.add("coil, spiral, volute, whorl, helix");
        classes.add("corkscrew, bottle screw");
        classes.add("cradle");
        classes.add("crate");
        classes.add("crib, cot");
        classes.add("Crock Pot");
        classes.add("diaper, nappy, napkin");
        classes.add("dishrag, dishcloth");
        classes.add("dishwasher, dish washer, dishwashing machine");
        classes.add("dome");
        classes.add("doormat, welcome mat");
        classes.add("Dutch oven");
        classes.add("electric fan, blower");
        classes.add("electric locomotive");
        classes.add("frying pan, frypan, skillet");
        classes.add("hand blower, blow dryer, blow drier, hair dryer, hair drier");
        classes.add("handkerchief, hankie, hanky, hankey");
        classes.add("hook, claw");
        classes.add("hourglass");
        classes.add("mixing bowl");
        classes.add("perfume, essence");
        classes.add("piggy bank, penny bank");
        classes.add("pill bottle");
        classes.add("pillow");
        classes.add("ping-pong ball");
        classes.add("plastic bag");
        classes.add("pole");
        classes.add("quilt, comforter, comfort, puff");
        classes.add("rule, ruler");
        classes.add("saltshaker, salt shaker");
        classes.add("screwdriver");
        classes.add("soap dispenser");
        classes.add("table lamp");
        classes.add("teapot");
        classes.add("teddy, teddy bear");
        classes.add("toilet seat");

        this.categoryMap.put("Household items", classes);
        mapHouseholdItemsOffice();
        mapHouseholdItemsFurniture();
    }

    private void mapHouseholdItemsOffice() {
        List<String> classes = new ArrayList<>();
        classes.add("ballpoint, ballpoint pen, ballpen, Biro");
        classes.add("fountain pen");
        classes.add("letter opener, paper knife, paperknife");
        classes.add("quill, quill pen");
        classes.add("pencil sharpener");
        classes.add("rubber eraser, rubber, pencil eraser");
        classes.add("pencil box, pencil case");

        this.categoryMap.put("Office items", classes);
    }

    private void mapHouseholdItemsFurniture() {
        List<String> classes = new ArrayList<>();
        classes.add("bookcase");
        classes.add("bookshop, bookstore, bookstall");
        classes.add("china cabinet, china closet");
        classes.add("desk");
        classes.add("dining table, board");
        classes.add("file, file cabinet, filing cabinet");
        classes.add("safe");

        this.categoryMap.put("Furniture", classes);
    }

    private void mapOutdoors() {
        List<String> classes = new ArrayList<>();
        classes.add(
                "backpack, back pack, knapsack, packsack, rucksack, haversack");
        classes.add("bicycle-built-for-two, tandem bicycle, tandem");
        classes.add("binoculars, field glasses, opera glasses");
        classes.add("chain");
        classes.add("chainlink fence");
        classes.add("chain saw, chainsaw");
        classes.add("combination lock");
        classes.add("dogsled, dog sled, dog sleigh");
        classes.add("folding chair");
        classes.add("grille, radiator grille");
        classes.add("hammer");
        classes.add("hard disc, hard disk, fixed disk");
        classes.add("hatchet");
        classes.add("holster");
        classes.add("-lantern\",");
        classes.add("jigsaw puzzle");
        classes.add("jinrikisha, ricksha, rickshaw");
        classes.add("lawn mower, mower");
        classes.add("lighter, light, igniter, ignitor");
        classes.add("lipstick, lip rouge");
        classes.add("lotion");
        classes.add("matchstick");
        classes.add("measuring cup");
        classes.add("mountain bike, all-terrain bike, off-roader");
        classes.add("mountain tent");
        classes.add("power drill");
        classes.add("radiator");
        classes.add("snorkel");
        classes.add("tricycle, trike, velocipede");
        classes.add("umbrella");
        classes.add("unicycle, monocycle");

        this.categoryMap.put("Outdoors", classes);
    }

    private void mapSports() {
        List<String> classes = new ArrayList<>();
        classes.add("barbell");
        classes.add("barber chair");
        classes.add("baseball");
        classes.add("basketball");
        classes.add("bathing cap, swimming cap");
        classes.add("bow");
        classes.add("dumbbell");
        classes.add("football helmet");
        classes.add("golf ball");
        classes.add("golfcart, golf cart");
        classes.add("pool table, billiard table, snooker table");
        classes.add("punching bag, punch bag, punching ball, punchball");
        classes.add("racket, racquet");
        classes.add("rugby ball");
        classes.add("soccer ball");
        classes.add("tennis ball");
        classes.add("volleyball");

        this.categoryMap.put("Sports", classes);
    }

    private void mapAutomotive() {
        List<String> classes = new ArrayList<>();
        classes.add("go-kart");
        classes.add("tow truck, tow car, wrecker");
        classes.add("tractor");
        classes.add("trailer truck, tractor trailer, trucking rig, rig, articulated lorry, semi");

        this.categoryMap.put("Automotive", classes);
        mapAutomotiveCars();
        mapAutomotiveMotorbikes();
        mapAutomotiveParts();
    }

    private void mapAutomotiveCars() {
        List<String> classes = new ArrayList<>();
        classes.add("racer, race car, racing car");
        classes.add("limousine, limo");
        classes.add("sports car, sport car");
        classes.add("beach wagon, station wagon, wagon, estate car, beach waggon, station waggon, waggon");
        classes.add("convertible");
        classes.add("jeep, landrover");

        this.categoryMap.put("Cars", classes);
    }

    private void mapAutomotiveMotorbikes() {
        List<String> classes = new ArrayList<>();
        classes.add("moped");
        classes.add("motor scooter, scooter");

        this.categoryMap.put("Motorbikes", classes);
    }

    private void mapAutomotiveParts() {
        List<String> classes = new ArrayList<>();
        classes.add("car mirror");
        classes.add("car wheel");
        classes.add("disk brake, disc brake");

        this.categoryMap.put("Automotive parts", classes);
    }

}
