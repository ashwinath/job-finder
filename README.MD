# Job Finder

Want to find a job but too lazy to find it yourself? Or maybe contest with HR's automated resume filtering systems? Then this is for you. Let circuitry handle the tedious job of searching for jobs! However this does not help you to auto apply for them. It's easy to implement if you do require the function.

## Current Implemented Strategies

Each strategy should implement how selenium should scrape the platform. Only Techinasia strategy has been implemented so far.

Scraping strategies are pretty easy. Just extend `ScrapingStrategy` and implement it.

## Configurations

Look into SystemConfig table of database. Configurations available:

* Company Name Filter (multiple allowed)
* Job Description Filters (multiple allowed)
* Negative Job Description Filters (multiple allowed)
* Min & Max Experience Fitlers
* Min & Max Pay Fitlers
* Selenium headless mode (headless by default, set to FALSE to see the action).
* Base url to search for each strategy (Base url refers to the listing page after searching).


## Building

Issue the command `./scripts/build.sh`.

## Running

Issue the command `java -jar /path/to/jar`.

## Filters

To implement a filter, just extend `Filter`.

Filters implemented:

* Company Name
* Job Description Filters (exclusion and inclusion)
* Max & Min Experience
* Max & Min Salary

## License
Copyright 2017 Ashwin Nath Chatterji

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
