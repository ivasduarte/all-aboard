    package domain;

    public class BuildingType {

        private final String name;

        private final int cost;

        private final String effect;

        private final int AvailableYear;

        public BuildingType(String name, int cost, String effect, int AvailableYear) {

            this.name = name;

            this.cost = cost;

            this.effect = effect;

            this.AvailableYear = AvailableYear;

        }

        public String getName() {

            return name;

        }

        public int getCost() {

            return cost;

        }

        public String getEffect() {

            return effect;

        }

        public int getAvailableYear() {

            return AvailableYear;

        }


    }
