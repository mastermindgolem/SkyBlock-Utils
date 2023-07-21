package com.golem.skyblockutils.command

import spock.lang.Specification

class CommandNameTest extends Specification {
    def "check if command names are returned correctly"(String valid, String expected) {
        when: "a command alias function is called"
        def alias = CommandName.cmdNameEquivalent valid
        then: "check if 'cmdNameEquivalent' returns correct output"
        alias == expected
        where: "a list of commands and desired outputs"
        valid              | expected
        "AttributeCommand" | "attributeprice"
        "EquipmentCommand" | "equipmentprice"
        "StatCommand"      | "kuudrastats"
        "UpgradeCommand"   | "attributeupgrade"
    }
}
