<h1 align="center"> DropBeat </h1>
<p align="center">
<a href="https://circleci.com/gh/pokk/DropBeat"><img src="https://circleci.com/gh/pokk/DropBeat.svg?style=svg"></a>
<a href="https://codebeat.co/projects/github-com-pokk-dropbeat-master"><img alt="codebeat badge" src="https://codebeat.co/badges/7079a1bd-6e84-4a94-bf8a-b30f7c509114" /></a>
<a href="https://www.codacy.com/manual/pokk/DropBeat?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pokk/DropBeat&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/7aa04a1604444d4592ce22da25b2299a"/></a>
<a href="https://www.codefactor.io/repository/github/smashks/stationmusicfm"><img src="https://www.codefactor.io/repository/github/smashks/stationmusicfm/badge" alt="CodeFactor" /></a>
<a href="https://codeclimate.com/github/pokk/DropBeat/maintainability"><img src="https://api.codeclimate.com/v1/badges/f9ef124dc3df905b7fc4/maintainability" /></a>
<a href="https://codeclimate.com/github/pokk/DropBeat/test_coverage"><img src="https://api.codeclimate.com/v1/badges/f9ef124dc3df905b7fc4/test_coverage" /></a>
<a href="https://opensource.org/licenses/MIT"><img alt="Licence" src="https://img.shields.io/badge/license-MIT-green.svg" />
</p>

# Introduction


# System Overview

## Module Dependency Graph

![Module Dependency Graph](https://user-images.githubusercontent.com/5198104/85551371-e7c64a80-b65c-11ea-9396-5537c8d22b21.png)

## Architecture of each module

We still would like to use the clean architecture with the dynamic feature so the mixed patteren happened here.

There are so many strict rules in the origin clean architecture, eg. an object on the **presentation layer** can't
access a class on the **data layer**, each layer has their own data classes, the mapper classes, ...etc.

If keeping the rules here, this project will be a hugh project and only one maintainer is me 😢. This project is still
keeping the basic rules but we need to have a compromise with those features.

![Feature Module Architecture](https://user-images.githubusercontent.com/5198104/85557159-7e493a80-b662-11ea-84e8-fc2e16198b21.png)

### Detail of Presentation Layer

- Activity/Fragment:
- ViewModel: It handles the view state and data, and the view logic. Also, it helps a view to connect a usecase. Three
  types of them, **ViewModel**, **AndroidViewModel**, **SavedStateViewModel** will be used.

### Detail of Domain Layer

- OneShot UseCase: Basically, this is one time fetching, just send a request and get a response, eg. normal restful api
  request.
- Observable UseCase: For the local database, once the data we are observing is changed, the obserable usecase will keep
  receiving the changing until the observing finishes.

### Detail of Data Layer

- Repository:
- 3 Layer Cache: We would like to have a cache system. One path is for fetching data from the remote server; another
  path is for the local database.

![Cache Strategy](https://user-images.githubusercontent.com/5198104/86508600-383b6600-be1c-11ea-8cc1-259930d5820b.png)
- Data Model:

# Licence

```
MIT License

Copyright (c) 2019 SmashKs

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

