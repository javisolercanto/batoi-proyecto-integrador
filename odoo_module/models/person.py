# -*- coding: utf-8 -*-
from openerp import models, fields, api

class person(models.Model):
    _name = 'batoi_logic.person'

    name = fields.Char(String='Full Name', size=70, required=True)
    nickname = fields.Char(String='Nickname', size=30, required=True)
    password = fields.Char(String='Password', required=True)
    email = fields.Char(String='E-mail', size=40)
    phone = fields.Integer(String='Telephone', size=9)

    _sql_constraints = [
        ('batoi_logic_unique_nickname', 'UNIQUE (nickname)', 'The nickname must be unique'),
        ('batoi_logic_unique_email', 'UNIQUE (email)', 'The E-mail must be unique')
    ]
