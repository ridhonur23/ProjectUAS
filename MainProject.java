import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Kelas dasar untuk Pengguna
class Pengguna {
    private String namaPengguna;
    private String kataSandi;

    public Pengguna(String namaPengguna, String kataSandi) {
        this.namaPengguna = namaPengguna;
        this.kataSandi = kataSandi;
    }

    public String getNamaPengguna() {
        return namaPengguna;
    }
    public String getKataSandi(){
        return kataSandi;
    }

    // Metode untuk menampilkan informasi pengguna
    public void tampilkanInfo() {
        System.out.println("Nama Pengguna: " + namaPengguna);
    }
}

// Kelas Admin yang mewarisi dari Pengguna
class Admin extends Pengguna {
    public Admin(String namaPengguna, String kataSandi) {
        super(namaPengguna, kataSandi);
    }

    // Metode untuk menampilkan semua informasi pengguna
    public void tampilkanSemuaInfoPengguna(Map<String, RegularUser> pengguna) {
        System.out.println("Semua Informasi Pengguna:");
        for (RegularUser user : pengguna.values()) {
            user.tampilkanInfo();
        }
    }

    // Metode untuk melihat info nasabah berdasarkan nama pengguna
    public void lihatInfoNasabah(String namaPengguna, Map<String, RegularUser> pengguna) {
        RegularUser nasabah = pengguna.get(namaPengguna);
        if (nasabah != null) {
            nasabah.tampilkanInfo();
        } else {
            System.out.println("Nasabah tidak ditemukan.");
        }
    }

    // Metode untuk melakukan operasi pada pengguna
    public void lakukanOperasiPadaPengguna(int operasi, Map<String, RegularUser> pengguna) {
        Scanner scanner = new Scanner(System.in);
        switch (operasi) {
            case 1:
                // Lihat info nasabah
                System.out.print("Masukkan nama pengguna Nasabah: ");
                String namaNasabah = scanner.next();
                lihatInfoNasabah(namaNasabah, pengguna);
                break;

            default:
                System.out.println("Operasi tidak valid.");
        }
    }
}

// Kelas RegularUser yang mewarisi dari Pengguna
class RegularUser extends Pengguna {
    private List<Transaksi> transaksi;
    private double saldo;

    public RegularUser(String namaPengguna, String kataSandi) {
        super(namaPengguna, kataSandi);
        this.transaksi = new ArrayList<>();
        this.saldo = 0.0;
    }

    // Metode untuk menampilkan informasi pengguna, saldo, dan riwayat transaksi
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("Saldo: " + saldo);
        System.out.println("Riwayat Transaksi:");
        for (Transaksi t : transaksi) {
            t.tampilkanInfo();
        }
    }

    // Metode untuk menambahkan transaksi
    public void tambahTransaksi(Transaksi transaksi) {
        this.transaksi.add(transaksi);
    }

    // Metode untuk menambah saldo
    public void tambahSaldo(double jumlah) {
        saldo += jumlah;
        Transaksi transaksi = new Transaksi("Tambah Saldo", jumlah);
        tambahTransaksi(transaksi);
    }

    // Metode untuk mengambil saldo
    public void ambilSaldo(double jumlah) {
        if (saldo >= jumlah) {
            saldo -= jumlah;
            Transaksi transaksi = new Transaksi("Ambil Saldo", jumlah);
            tambahTransaksi(transaksi);
            System.out.println("Saldo berhasil diambil.");
        } else {
            System.out.println("Saldo tidak mencukupi.");
        }
    }

    // Metode untuk menampilkan riwayat transaksi
    public void tampilkanRiwayatTransaksi() {
        System.out.println("Riwayat Transaksi:");
        for (Transaksi t : transaksi) {
            t.tampilkanInfo();
        }
    }
}

// Kelas Transaksi untuk merepresentasikan transaksi keuangan
class Transaksi {
    private String jenis;
    private double jumlah;

    public Transaksi(String jenis, double jumlah) {
        this.jenis = jenis;
        this.jumlah = jumlah;
    }

    // Metode untuk menampilkan informasi transaksi
    public void tampilkanInfo() {
        System.out.println("Jenis: " + jenis + ", Jumlah: " + jumlah);
    }
}

// Kelas Bank yang mewakili seluruh sistem perbankan
class Bank {
    private Map<String, RegularUser> pengguna;
    private Admin admin;

    public Bank() {
        this.pengguna = new HashMap<>();
        this.admin = new Admin("admin", "adminpass");
    }

    // Metode untuk menambahkan pengguna baru
    public void tambahPengguna(RegularUser pengguna) {
        this.pengguna.put(pengguna.getNamaPengguna(), pengguna);
    }

    // Metode untuk melakukan transaksi antar pengguna
    public void lakukanTransaksi(String pengirimNamaPengguna, String penerimaNamaPengguna, double jumlah) {
        RegularUser pengirim = pengguna.get(pengirimNamaPengguna);
        RegularUser penerima = pengguna.get(penerimaNamaPengguna);

        if (pengirim != null && penerima != null) {
            Transaksi transaksi = new Transaksi("Transfer", jumlah);
            pengirim.tambahTransaksi(transaksi);
            penerima.tambahTransaksi(transaksi);
            System.out.println("Transaksi berhasil!");
        } else {
            System.out.println("Nama pengguna tidak valid.");
        }
    }

    // Metode untuk menampilkan informasi pengguna berdasarkan jenis pengguna
    public void tampilkanInfoPengguna(String namaPengguna, boolean isAdmin) {
        if (isAdmin) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nOperasi Admin:");
            System.out.println("1. Lihat Info Semua Nasabah");
            System.out.println("2. Lakukan Operasi pada Nasabah");
            System.out.print("Masukkan pilihan Admin: ");
            int pilihanAdmin = scanner.nextInt();

            switch (pilihanAdmin) {
                case 1:
                    admin.tampilkanSemuaInfoPengguna(pengguna);
                    break;

                case 2:
                    admin.lakukanOperasiPadaPengguna(pilihanAdmin, pengguna);
                    break;

                default:
                    System.out.println("Pilihan Admin tidak valid.");
            }
        } else {
            RegularUser pengguna = getRegularUser(namaPengguna);
            if (pengguna != null) {
                pengguna.tampilkanInfo();
            } else {
                System.out.println("Pengguna tidak ditemukan.");
            }
        }
    }

    public RegularUser getRegularUser(String namaPengguna) {
        return pengguna.get(namaPengguna);
    }
}

// Kelas utama
public class MainProject {
    public static void main(String[] args) {
        Bank bank = new Bank();

        RegularUser pengguna1 = new RegularUser("user1", "pass1");
        RegularUser pengguna2 = new RegularUser("user2", "pass2");
        bank.tambahPengguna(pengguna1);
        bank.tambahPengguna(pengguna2);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Selamat datang di Aplikasi Catatan Keuangan!");

        while (true) {
            System.out.println("\n1. Masuk sebagai Admin");
            System.out.println("2. Masuk sebagai Pengguna");
            System.out.println("3. Keluar");
            System.out.print("Masukkan pilihan Anda: ");
            int pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    // Masuk sebagai Admin
                    System.out.print("Masukkan nama pengguna Admin: ");
                    String namaAdmin = scanner.next();
                    System.out.print("Masukkan kata sandi Admin: ");
                    String kataSandiAdmin = scanner.next();

                    if (namaAdmin.equals("admin") && kataSandiAdmin.equals("adminpass")) {
                        bank.tampilkanInfoPengguna(null, true);
                    } else {
                        System.out.println("Kredensial Admin tidak valid.");
                    }
                    break;

                case 2:
                    // Masuk sebagai Pengguna
                    System.out.print("Masukkan nama pengguna Pengguna: ");
                    String namaPengguna = scanner.next();
                    System.out.print("Masukkan kata sandi Pengguna: ");
                    String kataSandiPengguna = scanner.next();

                    RegularUser penggunaMasuk = bank.getRegularUser(namaPengguna);
                    if (penggunaMasuk != null && penggunaMasuk.getNamaPengguna().equals(namaPengguna)
                            && penggunaMasuk.getKataSandi().equals(kataSandiPengguna)) {

                        while (true) {
                            System.out.println("\n1. Lihat Informasi Saya");
                            System.out.println("2. Transfer Uang");
                            System.out.println("3. Logout");
                            System.out.println("4. Lihat Riwayat Transaksi");
                            System.out.println("5. Tambah Saldo");
                            System.out.println("6. Ambil Saldo");
                            System.out.print("Masukkan pilihan Anda: ");
                            int pilihanPengguna = scanner.nextInt();

                            switch (pilihanPengguna) {
                                case 1:
                                    // Lihat informasi pengguna
                                    penggunaMasuk.tampilkanInfo();
                                    break;

                                case 2:
                                    // Transfer uang
                                    System.out.print("Masukkan nama pengguna penerima: ");
                                    String namaPenerima = scanner.next();
                                    System.out.print("Masukkan jumlah untuk ditransfer: ");
                                    double jumlah = scanner.nextDouble();
                                    bank.lakukanTransaksi(namaPengguna, namaPenerima, jumlah);
                                    break;

                                case 3:
                                    // Logout
                                    System.out.println("Berhasil logout.");
                                    break;

                                case 4:
                                    // Lihat riwayat transaksi
                                    penggunaMasuk.tampilkanRiwayatTransaksi();
                                    break;

                                case 5:
                                    // Tambah saldo
                                    System.out.print("Masukkan jumlah saldo yang akan ditambahkan: ");
                                    double tambahanSaldo = scanner.nextDouble();
                                    penggunaMasuk.tambahSaldo(tambahanSaldo);
                                    System.out.println("Saldo berhasil ditambahkan.");
                                    break;

                                case 6:
                                    // Ambil saldo
                                    System.out.print("Masukkan jumlah saldo yang akan diambil: ");
                                    double penarikanSaldo = scanner.nextDouble();
                                    penggunaMasuk.ambilSaldo(penarikanSaldo);
                                    break;
                            }

                            if (pilihanPengguna == 3) {
                                break;
                            }
                        }
                    } else {
                        System.out.println("Kredensial Pengguna tidak valid.");
                    }
                    break;

                case 3:
                    // Keluar dari program
                    System.out.println("Keluar dari Aplikasi Catatan Keuangan. Selamat tinggal!");
                    System.exit(0);

                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        }
    }
}
