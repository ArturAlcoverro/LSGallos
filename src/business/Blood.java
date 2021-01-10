package business;

public class Blood extends Battle {

    public Blood(Theme theme, Rapper rapper1, Rapper rapper2) {
        super(theme, rapper1, rapper2);
    }

    /**
     * Funció que crida el metode run de la classe pare Battle
     */
    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
    }

    /**
     * @param numRimes Numero de rimes que ha aconseguit el raper en aquella batalla
     * @return Calcul de la puntuació aconseguida en funció del número de rimes
     */
    @Override
    public double calculateScore(int numRimes){
        double score = (Math.PI * Math.pow(numRimes, 2))/4;
        return score;
    }
}
