<h1 align="center"> DropBeat </h1>
<p align="center">
<a href="https://github.com/pokk/DropBeat/actions"><img src="https://github.com/pokk/DropBeat/workflows/Build Workflow/badge.svg"></a>
<a href="https://codebeat.co/projects/github-com-pokk-dropbeat-master"><img alt="codebeat badge" src="https://codebeat.co/badges/7079a1bd-6e84-4a94-bf8a-b30f7c509114" /></a>
<a href="https://www.codacy.com/manual/pokk/DropBeat?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pokk/DropBeat&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/7aa04a1604444d4592ce22da25b2299a"/></a>
<a href="https://www.codefactor.io/repository/github/pokk/dropbeat"><img src="https://www.codefactor.io/repository/github/pokk/dropbeat/badge" alt="CodeFactor" /></a>
<a href="https://codeclimate.com/github/pokk/DropBeat/maintainability"><img src="https://api.codeclimate.com/v1/badges/f9ef124dc3df905b7fc4/maintainability" /></a>
<a href="https://deepsource.io/gh/pokk/DropBeat/?ref=repository-badge" target="_blank"><img alt="DeepSource" title="DeepSource" src="https://deepsource.io/gh/pokk/DropBeat.svg/?label=active+issues&show_trend=true"/></a>
<a href="https://codecov.io/gh/pokk/DropBeat"><img src="https://codecov.io/gh/pokk/DropBeat/branch/master/graph/badge.svg?token=97V0QNONOT"/></a>
<a href="https://opensource.org/licenses/MIT"><img alt="Licence" src="https://img.shields.io/badge/license-MIT-green.svg" />
</p>

# System Overview

## Module Dependency Graph

![DropABeat Architecture@2x](https://user-images.githubusercontent.com/5198104/119675341-e9554f80-be77-11eb-84a3-6fe1f6791dab.png)

## Architecture of each feature module

Basically, our project is using single activity and multiple fragments.

#### The Feature of Clean Architecture

The **clean architecture** known to everyone, the purpose is dividing each layer and make them independent. I will say
the **clean architecture** is separating modules **horizontally**.

#### The Feature of Dynamic Feature

**Dynamic Feature** were published by few yeas ago. Briefly, as the word says, it makes each feature to a module. I will
say the **dynamic feature** is separating modules **vertically**.

We decided to get both advantages with an easier way and mix them together to our project. To mix them, the **dynamic
feature** will be the **_main_** architecture and **clean architecture** will be inside each feature module.

There are so many strict rules in the origin clean architecture, e.g. an object on the **presentation layer** won't
access a class from the **data layer**, or each layer has their own data classes, the mapper classes, ...etc.

If keeping the rules here, this project will be a hugh project and only one maintainer is me 😢. This project is still
keeping the basic rules, but we need to have a compromise with those features.

![Feature Module Architecture](https://user-images.githubusercontent.com/5198104/85557159-7e493a80-b662-11ea-84e8-fc2e16198b21.png)

### Detail of Presentation Layer

- Activity/Fragment:
- **ViewModel**: It handles the view state and data, and the **view logic**. Also, it helps a view to connect a usecase.
  Three types of them, **ViewModel**, **AndroidViewModel**, **SavedStateViewModel** will be used.

### Detail of Domain Layer

The business logic will be here mostly.

- **OneShot UseCase**: This is one time fetching, just send a request and get a response, eg. normal restful api
  request.
- **Observable UseCase**: For the local database, once the data we are observing is changed, the observable usecases
  will keep receiving the changing until the observing finishes.

### Detail of Data Layer

- **Repository**:
- 3 Layer Cache: We would like to have a cache system. One path is for fetching data from the remote server; another
  path is for the local database.

![Cache Strategy](https://user-images.githubusercontent.com/5198104/86508600-383b6600-be1c-11ea-8cc1-259930d5820b.png)

- Data Model:

# Snapshot of App

|                                                                                                                |                                                                                                                |                                                                                                                |                                                                                                                |
| :------------------------------------------------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------- |
| ![image](https://user-images.githubusercontent.com/5198104/120093618-76d5bf80-c156-11eb-901b-63d62e9cb746.png) | ![image](https://user-images.githubusercontent.com/5198104/120093637-92d96100-c156-11eb-9c62-262c992748c3.png) | ![image](https://user-images.githubusercontent.com/5198104/120093716-e9df3600-c156-11eb-91a9-1b1b643ed0a2.png) | ![image](https://user-images.githubusercontent.com/5198104/120093988-ac7ba800-c158-11eb-897c-4e7c25aaa0b0.png) |
| ![image](https://user-images.githubusercontent.com/5198104/120093667-b8666a80-c156-11eb-8b63-b2c2c2e6fed2.png) | ![image](https://user-images.githubusercontent.com/5198104/120093677-c61bf000-c156-11eb-9cd3-5c5753584f1d.png) | ![image](https://user-images.githubusercontent.com/5198104/120093689-d338df00-c156-11eb-829d-d6679e25c118.png) |                                                                                                                |

This is test

# Licence

```
MIT License

Copyright (c) 2021 Jieyi

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
