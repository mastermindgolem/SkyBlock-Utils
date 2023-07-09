<!--Test-->
<link href="./styles.css" rel="stylesheet" type="text/css">

# Skyblock Utilities!

<div align="center">
    <!--Download link-->
    <a href="https://sbu.kami-x.tk"><img alt="Click Me" src="https://img.shields.io/badge/SkyblockUtils-Click%20Me-191919?logo=Oracle" height=50></a>
    <!--164 downloads as of today-->
    <a href="https://sbu.kami-x.tk"><img src="https://img.shields.io/github/downloads/mastermindgolem/Skyblock-Utils/1.0.0/total?style=social&label=Downloads" alt="Download amount of 1.0.0"></a>
    <!--
        To get current lines since tokei seems to be not working for other sources such as         
        https://img.shields.io/tokei/lines/github/ascopes/java-compiler-testing
        This is our adaptation that I've made up using a mini bash script -Kami
        I will be adding an endpoint later so that this can automatically update with a cron job using again bash!
    The command to get entire lines is below but the script will prob just be another branch with the only nescessity folders written by us and would be shorten to `git ls-files | xargs wc -l`
    `git ls-files | grep -v -e 'src/test/**' -e "src/main/resources/assets/golemmod/logger/**" -e "**/*.ogg" -e "**/*.png" -e "**/*.lang" |  xargs wc -l` -v to exclude, -e for pattern
    -->
    <a><img src="https://img.shields.io/badge/Lines-5402-331D2C"></a>
    <!-- license -->
    <a href="./LICENSE" target="_blank">
        <img src="https://img.shields.io/github/license/mastermindgolem/Skyblock-Utils?color=3F2E3E&flat-square" alt="license">
    </a>
</div>


[//]: # (<!--add future download link-->)
<!--### [![Cool text](https://img.shields.io/badge/SkyblockUtils-Click%20Me-8A2BE2?logo=Oracle)](https://github.com/mastermindgolem/SkyBlock-Utils/releases/tag/1.0.0)-->

Skyblock utils <span title="May add other possible features!">currently<sub class="lb">*</sub></span> hovers around **kuudra related** features. [(Table of Contents)](#table-of-contents) 

## Table of Contents
- Features
    - [Chest pricing](#inventory-pricing) 
    - [Kuudra attribute Overlay](#kuudra-overlay)
    - [Party stats overall on Monke Finder](#kuudra-stats) 
    - [Show new member stats on join!](#on-join) 
    - [**PROFIT CALCULATOR**](#profit-calculator)
- Commands
    - [Main](#main) 
    - [Lbin of attribute (**armor**)](#attribute-pricing)
    - [Lbin of attribute (**equipment**)](#equipment-pricing)
    - [Estimates pricing to upgrade gear](#armor-upgrade)
    - [Show kuudra stats!](#viewing-kuudra-related-stats-and-items)
    - [Miscellaneous](#others) 

### Features
#### Inventory Pricing
This title is a little misleading! It shows the price of the current chest be it echest, paid chest, a chest on island! We'll add one for inventory soon! <br>
![ContainerPricing.png](images/ContainerPricing.png)

#### Kuudra Overlay
Shows the best attribute on the armour piece!<br>
[Read all about the kuudra overlay!]()<br>
LBIN is the armour is more expensive than the value of the attribute! 
[Read on comparison here!]()<br>

![AttributeOverlay.png](images/AttributeOverlay.png)

#### Kuudra stats
Hover over a party and press key-bind to check the party stats!
[Image soon]()

### On join
When a party member joins, their party stats will automagically appear in chat! _If you are wondering how, it's magic!_
[Not showing? Troubleshoot here](#toadd)

### PROFIT CALCULATOR
Of course saving the best for last! <br>
THE PROFIT CALCULATOR!! Self-explanatory
![PROFIT.png](images/PROFIT.png)

### Commands
#### Main
`/sbu` Shows gui <br>
`/sbu help` Sends help message in chat!

#### Attribute Pricing
##### Armour pricing
`/ap ah`
`/ap attribute level`
`/ap attribute attribute`
`/ap help`
[For more in-depth info about the arguments. Click me!](#toadd)

##### Equipment pricing
`/ap ah`
`/ap attribute level`
`/ap attribute attribute`
`/ap help`
[For more in-depth info about the arguments. Click me!](#toadd)

#### Armor Upgrade
`/au attribute`
`/au attribute level`
`/au attribute startlvl endlvl`
`/au help` 
[For more in-depth info about the arguments. Click me!](#toadd)

#### Viewing Kuudra related stats and items
`/ks`
`/ks ign`
`/ks help`
[For more in-depth info about the arguments. Click me!](#toadd)

#### Others
`/alias`

<a href="https://github.com/mastermindgolem/SkyBlock-Utils/releases/tag/1.0.0"><img alt="Static Badge" src="https://img.shields.io/badge/Made%20by-golem%2C%20Kami-8A2BE2?logo=caffeine"></a>
