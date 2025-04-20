class Hotel{
    //attributes of the hotel
    String Name;
    Integer id;
    
    // instead of a string here, we will keep it in a seperate class, 
    // so that we can use the class everywhere else we need to and also updating it would be easier, 
    // since we only need to update it at one place
    // this helps decouple the code
    Location hotelLocation;

    List<Room> roomList;
}

class Location{
    String city;
    String state;
    String country;
    string street;
    int pinCode;
}

class Room{
    // can use UUID here too
    String roomNumber;

    //Enum for room types
    RoomStyle roomStyle;
    //Enum for room status
    RoomStatus roomStatus;
    //Enum for room Prices
    Double bookingPrice;

    //to check the keys status(availability)
    list<RoomKey> roomKeys;

    // to check when it was cleaned last
    list<HouseKeepingLog> houseKeepingLogs;

}

public enum RoomStyle{
    STANDARD, DELUXE, FAMILY_SUITE;
}

public enum RoomStatus{
    AVAILABLE, RESERVED, NOT_AVAILABLE, OCCUPIED,  UNDER_MAINTENANCE;
}

class RoomKey{
    String keyId;
    String barCode;
    Date issuedAt;
    boolean isActivee;
    boolean isMaster;

    //a method to assign the key to a room
    public void assignRoom(Room room);

}

class HouseKeepingLog{
    String description;
    Date startDate;
    int duration;
    HouseKeeper houseKeeper;

    public void addRom(Room room);
}

// we can use inheritence here so lets have a person class as base class 
// and use subclasses for the receptionist, and the guest, and the admin, and the housekeeping staff

//for inheritence: to push some of the responsibilities to the to the base class
abstract class person{
    string name;
    Account accountDetails;
    String phone;
}


public class Account{
    
    string userName;
    string password;
    AccountStatus accountStatus;
} 

public enum AccountStatus{
    ACTIVE, INACTIVE, BLOCKED;
}

// all the classes for our actors: they all will exxtend the person base class

class houseKeeper extends person{
    public List<Room> public getRoomsServiced(Date date);
}

class Guest extends person{
    Search searchObj;
    Booking bookingObj;
    
}

class Receptionist extends person{
    Booking bookingObj;
    Search searchObj;

    public void checkIn(Guest guest, RoomBooking bookingInfo);
    public void checkOut(Guest guest, RoomBooking bookingInfo);
}

class admin extends person{
    public void addRoom(Room roomDetail);
    public void deleteRoom(string roomId);
    public void editRoom(Room roomDetail);
}

class search{
    public List searchRoom(RoomStyle roomStyle, Date startDate, int duration)
}

class Booking{
    public RoomBooking createBooking(Guest guestInfo);
    public RoomBooking cancelBooking(int BookingId);
}

class RoomBooking{
    String bookingId;
    Date startDate;
    int durationInDays;
    List<Guest> guestList;
    List<Room> roomInfo;
    BaseRoomCharge totalRoomCharges;
}

// Decorator pattern for the room charges
interface BaseRoomCharge{
    double getCost();
}

interface RoomCharge implements BaseRoomCharge{
    double cost;
    double getCost(){
        return cost;
    }

    void setCost(double cost){
        this.cost = cost;
    }
}

class RoomServiceCharge implements BaseRoomCharge{
    double cost;
    BaseRoomCharge baseRoomCharge;
    Double getCost(){
        baseRoomCharge.setCost(baseRoomCharge.getCost() + cost);
        return baseRoomCharge.getCost();
    }
}

class InRoomPurchasesCharges implements BaseRoomCharge{
    double cost;
    BaseRoomCharge baseRoomCharge;
    Double getCost(){
        baseRoomCharge.setCost(baseRoomCharge.getCost() + cost);
        return baseRoomCharge.getCost();
    }
}

