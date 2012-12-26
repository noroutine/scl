/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 2/27/11
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */

// Man, Friend, BestFriend, Dog
// Type, variable name

public class Man {
    BestFriend<?> bestFriend;

    Man manHasABestFriendDog() {
        Man man = new Man();
        man.bestFriend = new BestFriend<Dog>();

    }
}


class BestFriend<T> {
    T instance;

    BestFriend(String name) {

    }

    public String getName() {
        instance.getName();
    }
}

class Dog {
    String name;

    Dog(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}