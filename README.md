# Medibank News App

## Notes

Architecture: I'm using MVVM with LiveData on the view layer and Rx on the data layer. The conversion from Rx land to Livedata land is done in the view models. The overall architecture is a slight variation of "Clean Architecture" which is why you'd see UseCases being used instead of Repositories. If there was more than one data source (eg: api and local db), this could be handled by adding Repositories as a layer under use cases or the use cases themselves could handle it.

Eg: 
1. Viewmodel -> UseCase (has no knowledge on datasources) -> Repository (manages the data sources) -> api or local db
or
2. Viewmodel -> UseCase (handled datasources) -> api or local db

Either method can be selected depending on how much abstraction is needed and how complicated the app is. For a simple project like this, method 2 is sufficient even with a local data source.


## Some compromises due to the lack of time

- I haven't used Dagger for DI and just did manual DI from the App class
- I didn't use a local db like Room or Realm. Persisting saved news articles and sources would work better with a db rather than shared prefs.
- Unit tests are not 100% exhaustive. Just added some minimal test cases
- Would have liked to use 2 navigation graphs for the home page and detailed page but there are some tedious workarounds to do, to get that working
