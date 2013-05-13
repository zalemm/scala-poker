package spoker

package object game {

  import spoker.cards._
  import spoker.cards.Rank._

  object Game extends StraightAwareness with FlushAwareness {
    def apply(implicit cards:Cards): Game = {
      toTuples(cards.sorted) match {
        case (Seq((Ten,_),(Jack,_),(Queen,_),(King,_),(Ace,_))) if flush => new RoyalFlush
        case (_) if straight && flush => new StraightFlush
        case (_) => new HighCard
      }
    }
  }

  trait StraightAwareness {
    def straight(implicit cards:Cards) = {
      val ranks = cards.map(_.rank)
      ranks.takeRight(ranks.size-1).zip(ranks)
        .forall((t) => t._1.id == 1+t._2.id)
    }
  }

  trait FlushAwareness {
    def flush(implicit cards:Cards) = {
      cards.map(_.suit).forall(_ == cards.head.suit)
    }
  }

  sealed abstract class Game(cards: Cards)

  class RoyalFlush(implicit cards: Cards) extends Game(cards)
  class StraightFlush(implicit cards: Cards) extends Game(cards)
  class HighCard(implicit cards: Cards) extends Game(cards)
}