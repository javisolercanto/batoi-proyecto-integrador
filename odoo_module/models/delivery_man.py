# -*- coding: utf-8 -*-
from openerp import models, fields, api

class delivery_man(models.Model):
    _name = 'batoi_logic.delivery_man'
    _inherit = 'batoi_logic.person'

    routes_id = fields.One2many('batoi_logic.route', 'deliveryman_id', 'Routes to do')
    location_id = fields.One2many('batoi_logic.location', 'delivery_man_id', 'Location')
    lorry_id = fields.Many2one('batoi_logic.lorry', 'Lorry', required=True)

    # One2one -->
    _sql_constraints = [
        ('one2One_lorry', 'unique("lorry_id")',
         'Lorry must be unique, because is a One2one relationship!'),
    ]
