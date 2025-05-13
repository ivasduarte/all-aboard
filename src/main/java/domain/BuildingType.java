    package domain;

    public class BuildingType {

        private final String name;

        private final int cost;

        private final String effect;

        public BuildingType(String name, int cost, String effect){

            this.name = name;

            this.cost = cost;

            this.effect = effect;

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


    }
