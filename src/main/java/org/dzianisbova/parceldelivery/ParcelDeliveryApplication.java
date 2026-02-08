package org.dzianisbova.parceldelivery;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Vehicle;
import org.dzianisbova.parceldelivery.packing.algorithm.Packing;
import org.dzianisbova.parceldelivery.packing.model.VehiclePackingResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class ParcelDeliveryApplication {
    private static final Random RANDOM = new Random(12345);

    public static void main(String[] args) {
        SpringApplication.run(ParcelDeliveryApplication.class, args);
    }

    @Bean
    public CommandLineRunner runPackingDemo() {
        return args -> {
            try {
                System.out.println("\n=== STARTING PACKING TEST ===\n");

                Vehicle van = new Vehicle("VAN-001", new Dimensions(320, 178, 185), 1045);
                System.out.println("Vehicle created");

                List<Parcel> parcels = generateChallengingParcels(van);
                System.out.println("Parcels generated: " + parcels.size());

                Packing algorithm = new Packing();
                System.out.println("Algorithm initialized");

                long startTime = System.nanoTime();
                VehiclePackingResult result = algorithm.pack(parcels, van);
                long endTime = System.nanoTime();

                System.out.println("Packing completed");

                printResults(result, parcels, van, (endTime - startTime) / 1_000_000.0);

            } catch (Exception e) {
                System.err.println("ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    private List<Parcel> generateChallengingParcels(Vehicle vehicle) {
        List<Parcel> parcels = new ArrayList<>();

        double targetVolume = vehicle.getDimensions().volume();
        double currentVolume = 0;
        int parcelCount = 0;

        System.out.println("Target volume: " + targetVolume);

        while (currentVolume < targetVolume * 0.95) {
            double rand = RANDOM.nextDouble();
            Parcel parcel;

            if (rand < 0.20) {
                parcel = createParcel("P" + (++parcelCount), 10, 25, 35, 1, 3);
            } else if (rand < 0.45) {
                parcel = createParcel("P" + (++parcelCount), 20, 35, 50, 3, 8);
            } else if (rand < 0.70) {
                parcel = createParcel("P" + (++parcelCount), 30, 40, 55, 6, 12);
            } else if (rand < 0.90) {
                parcel = createParcel("P" + (++parcelCount), 45, 45, 60, 10, 18);
            } else {
                parcel = createParcel("P" + (++parcelCount), 55, 55, 75, 15, 25);
            }

            double parcelVolume = parcel.getDimensions().volume();

            if (currentVolume + parcelVolume <= targetVolume) {
                parcels.add(parcel);
                currentVolume += parcelVolume;

                if (parcelCount % 20 == 0) {
                    System.out.println("Generated " + parcelCount + " parcels, volume: " +
                            String.format("%.2f", currentVolume / 1_000_000) + " m³");
                }
            }

            if (parcelCount > 500) {
                System.out.println("Safety break at 500 parcels");
                break;
            }
        }

        System.out.println("Final: " + parcels.size() + " parcels, volume: " +
                String.format("%.2f", currentVolume / 1_000_000) + " m³");

        return parcels;
    }

    private Parcel createParcel(String id, double length, double width, double height,
                                double minWeight, double maxWeight) {
        double weight = minWeight + RANDOM.nextDouble() * (maxWeight - minWeight);
        return new Parcel(id, new Dimensions(length, width, height), weight);
    }

    private void printResults(VehiclePackingResult result, List<Parcel> parcels,
                              Vehicle vehicle, double timeMs) {
        int packed = result.getPlacements().size();

        double usedVolume = result.getPlacements().stream()
                .mapToDouble(p -> p.getParcel().getDimensions().volume())
                .sum();

        double totalParcelVolume = parcels.stream()
                .mapToDouble(p -> p.getDimensions().volume())
                .sum();

        double totalVolume = vehicle.getDimensions().volume();
        double volumeUtilization = (usedVolume / totalVolume) * 100;
        double packingRate = (packed * 100.0) / parcels.size();
        double theoreticalFit = (totalParcelVolume / totalVolume) * 100;

        System.out.println("\n=== PACKING EFFICIENCY ===");
        System.out.println("Parcels packed: " + packed + " / " + parcels.size() +
                " (" + String.format("%.1f%%", packingRate) + ")");
        System.out.println("Total parcel volume: " + String.format("%.2f m³", totalParcelVolume / 1_000_000) +
                " (" + String.format("%.1f%% of vehicle", theoreticalFit) + ")");
        System.out.println("Space efficiency: " + String.format("%.1f%%", volumeUtilization));
        System.out.println("Execution time: " + String.format("%.2f ms", timeMs));
    }
}
