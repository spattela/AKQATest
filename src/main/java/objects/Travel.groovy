package objects

class Travel {

    String type, fromDate, toDate, numberOfAdults, numberOfChildren, destination, mainTravelerDOB, marketingOption

    Map<String, String> mapTransformation(Travel travelObject) {

        Map<String, String> travelMapObject = [:]

        travelMapObject = ['type of cover'     : travelObject.type, 'number of adults': travelObject.numberOfAdults,
                           'number of children': travelObject.numberOfChildren, 'main traveller dob': travelObject.mainTravelerDOB]

        if (travelObject.fromDate) {
            travelMapObject.put('destination', travelObject.destination)
            travelMapObject.put('leave date', travelObject.fromDate)
            travelMapObject.put('return date', travelObject.toDate)
        }

        travelMapObject
    }
}


