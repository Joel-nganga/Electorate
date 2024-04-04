class ElectionMain {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> Electorate.main(null)).start();
        }
    }
}
