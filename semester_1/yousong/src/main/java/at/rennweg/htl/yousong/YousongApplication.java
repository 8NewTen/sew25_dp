package at.rennweg.htl.yousong;

import at.rennweg.htl.yousong.model.Song;
import at.rennweg.htl.yousong.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YousongApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(YousongApplication.class, args);
	}

	@Autowired
	private SongRepository songRepository;

	@Override
	public void run(String... args) throws Exception {
		Song song1 = Song.builder()
				.title("IMMER")
				.artist("Ski Aggu")
				.genre("German Hip Hop")
				.length(141)
				.build();

		Song song2 = Song.builder()
				.title("Starlight")
				.artist("Dave")
				.genre("Hip Hop")
				.length(213)
				.build();

		Song song3 = Song.builder()
				.title("Vampire Hour")
				.artist("Ken Carson")
				.genre("Trap")
				.length(140)
				.build();

		songRepository.save(song1);
		songRepository.save(song2);
		songRepository.save(song3);

	}
}
