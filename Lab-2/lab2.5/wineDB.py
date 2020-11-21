from pymongo import *

# The database used is from https://gist.github.com/ajubin/d331f3251db4bd239c7a1efd0af54e38
# db: wine_db
# collection: rest

CLIENT = MongoClient()

def get_all_wines_names(collection):
    for w in collection.find({}, {"title": 1}):
        print("\n", w)

def search_wine(collection, wine_name):
    for w in collection.find({"title": wine_name}):
        print(w)


def find_wines_defined_points(collection, points):
    for w in collection.find({'points': {'$eq': points}}):
        print(w)

def wines_from_Portugal(collection):
    for w in collection.find({'country': {'$eq': "Portugal"}}):
        print(w)

def wines_between_price(collection, low, high):
    for w in collection.find({'$and': [{'price' : {'$gte': low}},{'price': {'$lte': high}}]}):
        print(w)

def tasters_twitters(collection):
    for w in collection.find({}, {"taster_twitter_handle": 1}):
        print("\n", w)

def most_common_country(collection):
    for w in collection.aggregate([{'$group' : { '_id' : '$country', 'n' : {'$sum' : 1}}}, {'$sort': {'n': -1}}, {'$limit': 1}]):
        print(w)

def wines_in_each_country(collection):
    for w in collection.aggregate([{'$group' : { '_id' : '$country', 'n' : {'$sum' : 1}}}]):
        print(w)

def wines_with_Serene_in_title(collection):
    for w in collection.aggregate([{'$match': {'title': {'$regex': 'Serene*'}}},{'$count': 'n'}]):
        print(w)

def wine_by_winery(collection):
    for w in collection.aggregate([{'$group' : { '_id' : '$winery', 'n' : {'$sum' : 1}}}]):
        print(w)

def wine_by_region(collection):
    for w in collection.aggregate([{'$group' : { '_id' : '$region', 'n' : {'$sum' : 1}}}]):
        print(w)

def different_wineries_France(collection):
    for w in collection.aggregate([{'$match': {'country': 'France'}},{'$group': {'_id': '$winery'}},{'$count': "n"}]):
        print(w)

def main():
    db = CLIENT["wine_db"]
    collection = db["rest"]
    
    # get_all_wines_names(collection)

    # search_wine(collection, "Domaine Fernand Engel 2016 Pinot Noir Rosé (Alsace)")

    # find_wines_defined_points(collection, 95)

    # wines_from_Portugal(collection)

    # wines_between_price(collection, 89, 115)

    # tasters_twitters(collection)

    # most_common_country(collection)

    # wines_in_each_country(collection)

    # wines_with_Serene_in_title(collection)

    # wine_by_region(collection)
     
    # wine_by_winery(collection)

    # different_wineries_France(collection)

if __name__ == '__main__':
    main()