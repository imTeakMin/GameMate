package com.example.gamescord.repository.gamemate;

import com.example.gamescord.domain.Gamemate;
import com.example.gamescord.domain.QGamemate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.gamescord.domain.QGamemate.gamemate;

@Repository
public class GameMateRepository {

    @Autowired
    private SDJpaGameMateRepository gameMateRepository;
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public GameMateRepository(EntityManager em) {
        this.em=em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void saveGamemate(Gamemate gamemate){
        gameMateRepository.save(gamemate);
    }

    public Gamemate findGamemateByUsersId(Long usersId,Long gamesId){
        return queryFactory.select(gamemate)
                .from(gamemate)
                .where(gamemate.users.id.eq(usersId),gamemate.games.id.eq(gamesId))
                .fetchOne();
    }

    public List<Gamemate> findGamematesByUsersName(String name){
        return queryFactory.select(gamemate)
            .from(gamemate)
            .where(gamemate.users.usersName.eq(name))
            .orderBy(gamemate.users.usersName.asc())
            .fetch();
    }

    public void updatePrice(Long usersId, Long gamesId, Long price){
        queryFactory.update(gamemate)
                .set(gamemate.price, price)
                .where(gamemate.users.id.eq(usersId), gamemate.games.id.eq(gamesId))
                .execute();
    }

    public void deleteGamemate(Gamemate gamemate){
        gameMateRepository.deleteById(gamemate.getId());
    }

}
